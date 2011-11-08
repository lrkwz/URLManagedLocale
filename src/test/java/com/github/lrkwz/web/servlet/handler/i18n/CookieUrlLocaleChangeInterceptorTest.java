package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.support.RequestContextUtils;

@ContextConfiguration
public class CookieUrlLocaleChangeInterceptorTest extends
		UrlLocaleChangeInterceptorTest {

	@Test
	public void cookieBasedTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET",
				"/somecontroller");

		Locale locale = Locale.ITALY;
		Cookie cookies = new Cookie("locale", locale.toString());
		request.setCookies(cookies);

		doGet(request, new MockHttpServletResponse());

		assertTrue(RequestContextUtils.getLocale(request) + "!=" + locale,
				RequestContextUtils.getLocale(request).equals(locale));
	}
}
