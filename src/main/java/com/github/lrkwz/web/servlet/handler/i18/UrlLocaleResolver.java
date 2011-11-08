package com.github.lrkwz.web.servlet.handler.i18;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;

public class UrlLocaleResolver implements LocaleResolver {

	//LocaleResolver localeResolver;

	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale) request
				.getAttribute(CookieLocaleResolver.LOCALE_REQUEST_ATTRIBUTE_NAME);
		return locale;
	}

	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		// TODO Auto-generated method stub

	}

}
