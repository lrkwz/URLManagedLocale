package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@ContextConfiguration
public abstract class Base {
	Logger logger = LoggerFactory
			.getLogger(Base.class);

	@Resource
	UrlLocaleChangeInterceptor interceptor;

	@Resource
	UrlLocaleResolver localeResolver;


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
