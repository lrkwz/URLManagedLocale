package com.github.lrkwz.web.servlet.handler.i18n;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.lrkwz.web.WebAttributes;
import com.github.lrkwz.web.savedrequest.HttpSessionRequestCache;

/**
 * Interceptor that allows for changing the current locale on every request, via
 * the request's URI.
 * 
 * see also
 * http://stackoverflow.com/questions/6708884/map-url-with-language-identifier
 * 
 * @author lrkwz
 * 
 */
public class UrlLocaleChangeInterceptor extends HandlerInterceptorAdapter
		implements InitializingBean {

	private static final String DEFAULT_LOCALE_ATTRIBUTE_NAME = "locale";
	private String localeAttributeName = DEFAULT_LOCALE_ATTRIBUTE_NAME;

	Logger logger = LoggerFactory.getLogger(UrlLocaleChangeInterceptor.class);

	private static final Pattern DEFAULT_LOCALE_URL_PATTERN = Pattern
			.compile("^/([a-z]{2,3}_[A-Z]{2})\\b"); // prova anche
													// \b([a-z]{2})\b

	private Pattern localePattern = DEFAULT_LOCALE_URL_PATTERN;

	private RequestCache requestCache = new HttpSessionRequestCache();
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private static final String DEFAULT_LOCALE_CHANGEURL = "/chooseLocale.html";
	private URI localeChangeURI;

	public static String LOCALE_BACKURL = "locale_backurl";

	public UrlLocaleChangeInterceptor() {
		setLocaleChangeURI(DEFAULT_LOCALE_CHANGEURL);
	}

	private Matcher matchLocalePattern(HttpServletRequest request) {
		final String requestURI = request.getServletPath();

		if (requestURI != null) {
			final Matcher matcher = localePattern.matcher(requestURI);
			if (matcher.find()) {
				return matcher;
			} else {
				logger.debug("Could not match {} against {}", localePattern,
						requestURI);
			}
		}
		return null;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (request.getServletPath().startsWith(localeChangeURI.getPath())) {
			logger.debug("This is the change locale page request: no handler processing");
			return true;
		}

		logger.debug(">>>>> requesting {}, redirect is {}",
				request.getServletPath(), localeChangeURI.getPath());

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);

		// Locale explicitly expressed in the URL has precedence
		Matcher matcher = matchLocalePattern(request);
		if (matcher != null) {
			final String locale = matcher.group(1);
			if (locale != null) {
				if (localeResolver == null) {
					throw new IllegalStateException(
							"No LocaleResolver found: not in a DispatcherServlet request?");
				}
				localeResolver.setLocale(request, response,
						StringUtils.parseLocaleString(locale));
			}
		} else {
			logger.debug("Locale has not been changed");
			localeResolver.resolveLocale(request);
		}

		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (savedRequest != null) {
			requestCache.removeRequest(request, response);

			// Use the DefaultSavedRequest URL
			String targetUrl = savedRequest.getRedirectUrl();
			logger.debug("Redirecting to DefaultSavedRequest Url: " + targetUrl);
			getRedirectStrategy().sendRedirect(request, response, targetUrl);
		}

		boolean mustRedirect = UrlLocaleResolver.isJVMLocale(request);
		if (mustRedirect) {
			requestCache.saveRequest(request, response);
			getRedirectStrategy().sendRedirect(request, response,
					getLocaleChangeURI());
		}

		// Proceed in any case.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
		if (modelAndView != null) {
			if (modelAndView.getModel() != null) {
				if (StringUtils.hasText(localeAttributeName)) {
					logger.info("Current locale has been stored in the model map has attribute named '{}'", localeAttributeName);
					modelAndView.getModelMap().addAttribute(
							localeAttributeName,
							RequestContextUtils.getLocaleResolver(request)
									.resolveLocale(request));
				}
			}
		}
	}

	public void setLocalePattern(final String localePattern) {
		Assert.isTrue(localePattern.matches(".*\\(.*\\).*"),
				"Pattern MUST contain a group expression");
		this.localePattern = Pattern.compile(localePattern);
	}

	public String getLocaleChangeURI() {
		return localeChangeURI.toString();
	}

	public void setLocaleChangeURI(String localeChangeURI) {
		try {
			logger.debug("Redirect uri is {}", localeChangeURI);
			this.localeChangeURI = new URI(localeChangeURI);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException(localeChangeURI
					+ " must be a valid URI: " + e, e);
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(localePattern);
		Assert.hasText(localePattern.toString());
		Assert.isTrue(localePattern.toString().matches(".*\\(.*\\).*"),
				"Pattern MUST contain a group expression");

		// TODO check if controlling localeChangeURI is necessary or not
	}

	/**
	 * Allows overriding of the behavior when redirecting to a target URL.
	 */
	public void setRedirectStrategy(RedirectStrategy redirectStrategy) {
		this.redirectStrategy = redirectStrategy;
	}

	protected RedirectStrategy getRedirectStrategy() {
		return redirectStrategy;
	}

	/**
	 * @return the localeAttributeName
	 */
	public String getLocaleAttributeName() {
		return localeAttributeName;
	}

	/**
	 * @param localeAttributeName the localeAttributeName to set
	 */
	public void setLocaleAttributeName(String localeAttributeName) {
		this.localeAttributeName = localeAttributeName;
	}

}
