package com.github.lrkwz.web.servlet.handler.i18n;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

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

	Logger logger = LoggerFactory.getLogger(UrlLocaleChangeInterceptor.class);

	private static final Pattern DEFAULT_LOCALE_URL_PATTERN = Pattern
			.compile("^/([a-z]{2,3}_[A-Z]{2})\\b"); // prova anche
													// \b([a-z]{2})\b

	private Pattern localePattern = DEFAULT_LOCALE_URL_PATTERN;

	private RequestCache requestCache = new HttpSessionRequestCache();

	private static final String DEFAULT_LOCALE_CHANGEURL = "/chooseLocale.html";
	private URI localeChangeURI;

	public static String LOCALE_BACKURL = "locale_backurl";

	public UrlLocaleChangeInterceptor() {
		setLocaleChangeURL(DEFAULT_LOCALE_CHANGEURL);
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

		logger.debug(">>>>> requesting {}, redirect is {}",
				request.getRequestURI(), localeChangeURI.getPath());
		if (request.getRequestURI().startsWith(localeChangeURI.getPath())) {
			logger.debug("This is the change locale page request: no handler processing");
			return true;
		}

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);

		// Locale explicitelly expressend in the url has precedence
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
		}
		// Proceed in any case.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (request.getRequestURI().startsWith(localeChangeURI.getPath())) {
			logger.debug("This is the change locale page request: no post-handler processing");
		} else {
			// final Matcher matcher = matchLocalePattern(request);
			if (RequestContextUtils.getLocaleResolver(request).resolveLocale(
					request) == null) {
				requestCache.saveRequest(request, response);
				if (localeChangeURI.getHost() == null) {
					if (!localeChangeURI.getPath().startsWith(
							request.getContextPath())) {
						setLocaleChangeURL(request.getContextPath()
								+ localeChangeURI.getPath());
					}
				}
				doRedirect(response, localeChangeURI, modelAndView);
			}
		}
	}

	private void doForward(HttpServletResponse response, String localeChangeURL2) {

	}

	public void setLocalePattern(final String localePattern) {
		Assert.isTrue(localePattern.matches(".*\\(.*\\).*"),
				"Pattern MUST contain a group expression");
		this.localePattern = Pattern.compile(localePattern);
	}

	private void setBackURL(HttpServletRequest request) {
		request.setAttribute(LOCALE_BACKURL, request.getRequestURI());
	}

	private void doRedirect(HttpServletResponse response, URI targetUrl,
			ModelAndView modelAndView) throws IOException {

		if (modelAndView != null) {
			RedirectView rView = new RedirectView(targetUrl.toString(), true);
			rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
			rView.setHttp10Compatible(false);
			rView.setExposeModelAttributes(false);
			modelAndView.setView(rView);
		}
		response.sendRedirect(targetUrl.toString());
	}

	public String getLocaleChangeURL() {
		return localeChangeURI.toString();
	}

	public void setLocaleChangeURL(String localeChangeURI) {
		try {
			logger.debug("REdirect uri is {}", localeChangeURI);
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
}
