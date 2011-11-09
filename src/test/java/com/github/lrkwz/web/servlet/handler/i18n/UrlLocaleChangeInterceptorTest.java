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
public class UrlLocaleChangeInterceptorTest extends
		Base {

	@Test
	public void testLocalizedUrl() throws Exception {
		final Locale locale = Locale.ITALY;
		final String requestURI = String.format("/%s/somecontroller",
				locale.toString());
		doGet(locale, requestURI);
	}

	@Test
	public void testWrongLocalizedUrl() throws Exception {
		final Locale locale = new Locale("xx", "YY");
		final String requestURI = String.format("/%s/somecontroller",
				locale.toString());
		doGet(locale, requestURI);
	}

	@Test
	public void testUnlocalizedUrl() throws Exception {
		final String requestURI = "/somecontroller";
		MockHttpServletResponse response = new MockHttpServletResponse();
		doGet(new MockHttpServletRequest("GET", requestURI), response);

		assertTrue(
				"Should redirect",
				response.getRedirectedUrl().compareTo(
						interceptor.getLocaleChangeURL()) == 0);
	}

}
