package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.github.lrkwz.web.servlet.handler.i18.UrlLocaleChangeInterceptor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration()
public class UrlLocaleChangeInterceptorTest {

	@Autowired
	UrlLocaleChangeInterceptor interceptor;

	@Autowired
	LocaleResolver localeResolver;

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
		final Locale locale = Locale.ITALY;
		final String requestURI = String.format("/somecontroller",
				locale.toString());
		doGet(locale, requestURI);
	}

 	private void doGet(final Locale locale, final String requestURI)
			throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();

		request.setRequestURI(requestURI);
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
				localeResolver);

		MockHttpServletResponse response = new MockHttpServletResponse();

		interceptor.preHandle(request, response, null);

		RequestContextUtils.getLocaleResolver(request);

		assertTrue(RequestContextUtils.getLocale(request) + "!=" + locale,
				RequestContextUtils.getLocale(request).equals(locale));
	}
}
