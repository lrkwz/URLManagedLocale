package com.github.lrkwz.web.servlet.handler.i18n;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

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
public class UrlLocaleChangeInterceptor extends HandlerInterceptorAdapter {

	private static final Pattern DEFAULT_LOCALE_URL_PATTERN = Pattern
			.compile("^/([a-z]{2,3}_[A-Z]{2})/"); // prova anche \b([a-z]{2})\b

	private Pattern localePattern = DEFAULT_LOCALE_URL_PATTERN;

	private String localeChangeURL;

	private Matcher matchLocalePattern(HttpServletRequest request) {
		final String requestURI = request.getRequestURI();
		final Matcher matcher = localePattern.matcher(requestURI);

		if (requestURI != null) {
			if (matcher.find()) {
				return matcher;
			}
		}
		return null;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

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
		}
		// Proceed in any case.
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		// final Matcher matcher = matchLocalePattern(request);
		if (RequestContextUtils.getLocaleResolver(request).resolveLocale(
				request) == null) {
			doRedirect(response, localeChangeURL, modelAndView);
		}
	}

	public void setLocalePattern(final String localePattern) {
		if (localePattern.matches(".*\\(.*\\).*") == false) {
			throw new IllegalArgumentException(
					"Your pattern needs to define a match group");
		}
		this.localePattern = Pattern.compile(localePattern);
	}

	private void doRedirect(HttpServletResponse response, String targetUrl,
			ModelAndView modelAndView) throws IOException {
		if (modelAndView != null) {
			RedirectView rView = new RedirectView(targetUrl, true);
			rView.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
			rView.setHttp10Compatible(false);
			rView.setExposeModelAttributes(false);
			modelAndView.setView(rView);
		}
		response.sendRedirect(targetUrl);
	}

	public String getLocaleChangeURL() {
		return localeChangeURL;
	}

	public void setLocaleChangeURL(String localeChangeURL) {
		this.localeChangeURL = localeChangeURL;
	}
}
