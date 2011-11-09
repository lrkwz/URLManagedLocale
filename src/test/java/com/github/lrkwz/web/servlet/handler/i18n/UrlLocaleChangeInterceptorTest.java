package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@ContextConfiguration
public abstract class UrlLocaleChangeInterceptorTest {
	Logger logger = LoggerFactory
			.getLogger(UrlLocaleChangeInterceptorTest.class);

	@Resource
	UrlLocaleChangeInterceptor interceptor;

	@Resource
	UrlLocaleResolver localeResolver;

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
		MockHttpServletResponse response = new MockHttpServletResponse();
		doGet(new MockHttpServletRequest("GET", requestURI), response);

		assertTrue(
				"Should redirect",
				response.getRedirectedUrl().compareTo(
						interceptor.getLocaleChangeURL()) == 0);
	}

	protected void doGet(final Locale locale, final String requestURI)
			throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET",
				requestURI);
		MockHttpServletResponse response = new MockHttpServletResponse();

		doGet(request, response);

		assertTrue(RequestContextUtils.getLocale(request) + "!=" + locale,
				RequestContextUtils.getLocale(request).equals(locale));
		assertTrue("No redirects here!", response.getRedirectedUrl() == null);
	}

	protected void doGet(MockHttpServletRequest request,
			MockHttpServletResponse response) throws Exception {
		logger.debug("Processing request {}", request.getRequestURI());
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
				localeResolver);

		interceptor.preHandle(request, response, null);

		interceptor.postHandle(request, response, null, new ModelAndView());
	}
}
