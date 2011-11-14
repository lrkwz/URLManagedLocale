package com.github.lrkwz.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.lrkwz.web.servlet.handler.i18n.UrlLocaleChangeInterceptor;

//@Controller
public class ChangeLocaleController {
	Logger logger = LoggerFactory.getLogger(ChangeLocaleController.class);
/*
	@RequestMapping("/chooseLocale.html")
	public void chooseLocale(HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(value = "locale") String locale) throws IOException {

		logger.debug("New locale will be {}", locale);

		LocaleResolver localeResolver = RequestContextUtils
				.getLocaleResolver(request);
		localeResolver.setLocale(request, response,
				StringUtils.parseLocaleString(locale));

	}
*/
}
