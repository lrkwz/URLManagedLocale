package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@ContextConfiguration
public abstract class Base {
	Logger logger = LoggerFactory.getLogger(Base.class);

	@Resource
	UrlLocaleChangeInterceptor interceptor;

	@Resource
	UrlLocaleResolver localeResolver;

	protected void processInterceptor(final Locale locale,
			final String servletPath, HttpSession session) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setContextPath("/appcontext");
		request.setServletPath(servletPath);
		if (session != null) {
			request.setSession(session);
		}
		MockHttpServletResponse response = new MockHttpServletResponse();

		processInterceptor(request, response);

		Locale newlocale = RequestContextUtils.getLocaleResolver(request)
				.resolveLocale(request);
		assertTrue("Locale should not be null", newlocale != null);
		assertTrue(newlocale + "!=" + locale, newlocale.equals(locale));
		assertTrue("No redirects here!", response.getRedirectedUrl() == null);
	}

	protected void processInterceptor(MockHttpServletRequest request,
			MockHttpServletResponse response) throws Exception {
		logger.debug("Processing request {}", request.getRequestURI());
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
				localeResolver);

		interceptor.preHandle(request, response, null);

		interceptor.postHandle(request, response, null, new ModelAndView());
	}
}
