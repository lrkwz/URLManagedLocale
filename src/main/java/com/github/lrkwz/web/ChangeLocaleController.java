package com.github.lrkwz.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.lrkwz.web.servlet.handler.i18n.UrlLocaleChangeInterceptor;

public class ChangeLocaleController {

	@RequestMapping("/changeLocale.html")
	public void chooseLocale(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "locale") String locale) throws IOException {

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		localeResolver.setLocale(request, response,
				StringUtils.parseLocaleString(locale));

		String backUrl = (String) request
				.getAttribute(UrlLocaleChangeInterceptor.LOCALE_BACKURL);
		if (backUrl != null) {
			response.sendRedirect(backUrl);
		}
	}

}
