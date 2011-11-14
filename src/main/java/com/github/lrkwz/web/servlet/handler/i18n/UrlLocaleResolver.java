package com.github.lrkwz.web.servlet.handler.i18n;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

import com.github.lrkwz.web.WebAttributes;

public class UrlLocaleResolver implements LocaleResolver {
	Logger logger = LoggerFactory.getLogger(UrlLocaleResolver.class);

	InternalCookieLocaleResolver localeResolver;

	public UrlLocaleResolver() {
		localeResolver = new InternalCookieLocaleResolver();
	}

	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale) request
				.getAttribute(WebAttributes.LOCALE_URL_ATTRIBUTE_NAME);
		if (locale == null) {
			request.setAttribute(WebAttributes.USING_JVM_LOCALE, "false"); //NB This MUST be placed _before_ the cookie.resolveLocale
			locale = localeResolver.resolveLocale(request);
			request.setAttribute(WebAttributes.LOCALE_URL_ATTRIBUTE_NAME,
					locale);
		}
		logger.debug("New locale is {}", locale != null ? locale.toString()
				: "null");
		return locale;
	}

	public static boolean isJVMLocale(ServletRequest request) {
		if (request.getAttribute(WebAttributes.USING_JVM_LOCALE) == null) {
			return true;
		} else {
			return Boolean.parseBoolean((String) request
					.getAttribute(WebAttributes.USING_JVM_LOCALE));
		}
	}

	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		localeResolver.setLocale(request, response, locale);

		request.setAttribute(WebAttributes.LOCALE_URL_ATTRIBUTE_NAME,
				locale);
		request.setAttribute(WebAttributes.USING_JVM_LOCALE, "false");
		logger.debug("Locale has been explicitely set to  {}", locale);
	}

	class InternalCookieLocaleResolver extends CookieLocaleResolver {

		@Override
		protected Locale determineDefaultLocale(HttpServletRequest request) {
			logger.info("No default locale for this resolver");
			request.setAttribute(WebAttributes.USING_JVM_LOCALE, "true");
			return super.determineDefaultLocale(request);
		}
	}

	/**
	 * @param cookieDomain
	 * @see org.springframework.web.util.CookieGenerator#setCookieName(java.lang.String)
	 */
	public void setCookieName(String cookieName) {
		localeResolver.setCookieName(cookieName);
	}

	/**
	 * @param cookieDomain
	 * @see org.springframework.web.util.CookieGenerator#getCookieName()
	 */
	public String getCookieName() {
		return localeResolver.getCookieName();
	}

	/**
	 * @param cookieDomain
	 * @see org.springframework.web.util.CookieGenerator#setCookieDomain(java.lang.String)
	 */
	public void setCookieDomain(String cookieDomain) {
		localeResolver.setCookieDomain(cookieDomain);
	}

	/**
	 * @param cookiePath
	 * @see org.springframework.web.util.CookieGenerator#setCookiePath(java.lang.String)
	 */
	public void setCookiePath(String cookiePath) {
		localeResolver.setCookiePath(cookiePath);
	}

	/**
	 * @param cookieMaxAge
	 * @see org.springframework.web.util.CookieGenerator#setCookieMaxAge(java.lang.Integer)
	 */
	public void setCookieMaxAge(Integer cookieMaxAge) {
		localeResolver.setCookieMaxAge(cookieMaxAge);
	}

	/**
	 * @param cookieSecure
	 * @see org.springframework.web.util.CookieGenerator#setCookieSecure(boolean)
	 */
	public void setCookieSecure(boolean cookieSecure) {
		localeResolver.setCookieSecure(cookieSecure);
	}
}
