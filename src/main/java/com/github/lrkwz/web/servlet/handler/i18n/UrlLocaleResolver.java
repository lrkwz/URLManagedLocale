package com.github.lrkwz.web.servlet.handler.i18n;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.view.RedirectView;

public class UrlLocaleResolver implements LocaleResolver {

	private static final String LOCALE_REQUEST_ATTRIBUTE_NAME = "URL_LOCALE_RESOLVED";

	LocaleResolver localeResolver;

	public Locale resolveLocale(HttpServletRequest request) {
		Locale locale = (Locale) request
				.getAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME);
		if (locale == null) {
			// redirect
		}
		return locale;
	}

	public void setLocale(HttpServletRequest request,
			HttpServletResponse response, Locale locale) {
		localeResolver.setLocale(request, response, locale);

		request.setAttribute(LOCALE_REQUEST_ATTRIBUTE_NAME, locale);
	}

	public void setLocaleResolver(LocaleResolver localeResolver) {
		this.localeResolver = localeResolver;
	}
}
