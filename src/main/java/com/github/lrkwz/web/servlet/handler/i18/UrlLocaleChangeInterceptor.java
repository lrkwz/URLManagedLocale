package com.github.lrkwz.web.servlet.handler.i18;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import junit.framework.Assert;

import static junit.framework.Assert.assertTrue;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

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

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		final String requestURI = request.getRequestURI();
		if (requestURI != null) {
			final Matcher matcher = localePattern.matcher(requestURI); // Prima avevo
										// request.getRequestURI()
			if (matcher.find()) {
				final String locale = matcher.group(1);
				if (locale != null) {
					LocaleResolver localeResolver = RequestContextUtils
							.getLocaleResolver(request);
					if (localeResolver == null) {
						throw new IllegalStateException(
								"No LocaleResolver found: not in a DispatcherServlet request?");
					}
					localeResolver.setLocale(request, response,
							StringUtils.parseLocaleString(locale));
				}
			}
		}

		// Proceed in any case.
		return true;
	}

	public void setLocalePattern(final String localePattern) {
		Assert.assertTrue("Your pattern needs to define a match group",
				localePattern.matches(".*\\(.*\\).*"));
		this.localePattern = Pattern.compile(localePattern);
	}
}
