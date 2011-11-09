package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.servlet.http.Cookie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.support.RequestContextUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class CookieUrlLocaleChangeInterceptorTest extends
		UrlLocaleChangeInterceptorTest {

	/*
	 * Simulates a call with pre-selected locale based on cookie
	 */
	@Test
	public void cookieBasedTest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET",
				"/somecontroller");

		final Locale locale = Locale.CANADA_FRENCH;
		Cookie cookies = new Cookie(localeResolver.getCookieName(),
				locale.toString());
		request.setCookies(cookies);

		MockHttpServletResponse response = new MockHttpServletResponse();
		doGet(request, response);

		assertTrue(RequestContextUtils.getLocale(request) + "!=" + locale,
				RequestContextUtils.getLocale(request).equals(locale));
		assertTrue("This should be null: " + response.getRedirectedUrl(),
				response.getRedirectedUrl() == null);
	}
}
