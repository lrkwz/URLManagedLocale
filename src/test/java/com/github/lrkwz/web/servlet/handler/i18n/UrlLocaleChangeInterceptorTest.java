package com.github.lrkwz.web.servlet.handler.i18n;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Locale;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.servlet.DispatcherServlet;

import com.github.lrkwz.web.ChangeLocaleController;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration
public class UrlLocaleChangeInterceptorTest extends Base {

	@Test
	public void testLocalizedUrl() throws Exception {
		final Locale locale = Locale.ITALY;
		final String requestURI = String.format("/%s/somecontroller",
				locale.toString());
		processInterceptor(locale, requestURI, null);
	}

	@Test
	public void testWrongLocalizedUrl() throws Exception {
		final Locale locale = new Locale("xx", "YY");
		final String requestURI = String.format("/%s/somecontroller",
				locale.toString());
		processInterceptor(locale, requestURI, null);
	}

	@Test
	public void testUnlocalizedUrl() throws Exception {
		final String requestURI = "/somecontroller";
		getURI(requestURI, null);
	}

	private MockHttpServletResponse getURI(final String requestURI,
			MockHttpSession session) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest("GET",
				requestURI);
		if (session != null) {
			request.setSession(session);
		}
		MockHttpServletResponse response = new MockHttpServletResponse();
		processInterceptor(request, response);

		assertTrue(
				"Should redirect",
				response.getRedirectedUrl().compareTo(
						interceptor.getLocaleChangeURI()) == 0);
		return response;
	}

	/*
	String callChooseLocaleController(HttpSession session,
			String chooseLocaleURI, String locale) throws IOException {
		MockHttpServletRequest request = new MockHttpServletRequest("GET",
				chooseLocaleURI);
		request.setSession(session);
		request.setAttribute(DispatcherServlet.LOCALE_RESOLVER_ATTRIBUTE,
				localeResolver);

		MockHttpServletResponse response = new MockHttpServletResponse();

		controller.chooseLocale(request, response, locale);
		return response.getRedirectedUrl();
	}

	@Resource
	ChangeLocaleController controller;

	@Test
	public void testRedirectAndBack() throws Exception {
		MockHttpSession session = new MockHttpSession();
		final String requestURI = "/somecontroller";

		MockHttpServletResponse response = getURI(requestURI, session);

		Locale locale = Locale.CHINA;
		callChooseLocaleController(session, response.getRedirectedUrl(),
				locale.toString());

		processInterceptor(locale, requestURI, session);
	}
	*/

}
