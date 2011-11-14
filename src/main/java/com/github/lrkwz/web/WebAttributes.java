package com.github.lrkwz.web;

import com.github.lrkwz.web.servlet.handler.i18n.UrlLocaleResolver;

public final class WebAttributes {
	// public static final String ACCESS_DENIED_403 =
	// "SPRING_SECURITY_403_EXCEPTION";
	// public static final String AUTHENTICATION_EXCEPTION =
	// "SPRING_SECURITY_LAST_EXCEPTION";
	// public static final String LAST_USERNAME =
	// "SPRING_SECURITY_LAST_USERNAME";
	public static final String SAVED_REQUEST = "SPRING_SECURITY_SAVED_REQUEST_KEY";
	public static final String LOCALE_URL_ATTRIBUTE_NAME = UrlLocaleResolver.class
			.getName() + ".LOCALE";
	public static final String USING_JVM_LOCALE = "using jvm locale";
}