package com.github.lrkwz.web.servlet.handler.i18;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

public class UrlLocaleInitializerInterceptor extends HandlerInterceptorAdapter {

	private static final Log log = LogFactory
			.getLog(UrlLocaleInitializerInterceptor.class);

	private String localeChangeUrl;

	public UrlLocaleInitializerInterceptor() {
		super();
	}

	/**
	 * Verifica che sia stato impostato il locale (language-country/market)
	 * altrimenti redirige alla pagina di scelta del locale (localeChangeUrl).
	 * 
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {

		if (request.getPathInfo().equals(getLocaleChangeUrl())) {
			return true;
		}

		/*
		// scatena il meccanismo per il recupero del locale
		// DEVE essere chiamato perche' il check seguente possa funzionare
		final Locale locale = RequestContextUtils.getLocale(request);

		if (log.isDebugEnabled()) {
			final String paramNames = StringUtils
					.collectionToCommaDelimitedString(Collections.list(request
							.getParameterNames()));
			log.debug("request.getPathInfo() is: "
					+ request.getPathInfo()
					+ " referer is: "
					+ request.getHeader("referer")
					+ (StringUtils.hasText(paramNames) ? " request parameters are: "
							+ paramNames
							: ""));
		}

		// recupero la pagina di destinazione
		String destination = getDestinationPage(request);

		try {
			// valorizzo il parametro di destinazione
			StringBuffer sb = new StringBuffer(128)
					.append(request.getContextPath())
					.append(getLocaleChangeUrl()).append("?").append("dest=")
					.append(URLEncoder.encode(destination, "UTF-8"));

			String redirect = sb.toString();
			log.debug("REDIRECT=" + redirect);

			// altrimenti redirigo alla pagina di scelta del locale
			response.setStatus(HttpServletResponse.SC_MULTIPLE_CHOICES);
			response.sendRedirect(redirect);
		} catch (IOException e) {
			log.debug("Exception handling", e);
			throw new ServletException(e);
		}
		*/

		log.debug("Current locale is " + LocaleContextHolder.getLocale());

		return true;
	}

	private static final Pattern LOCALE_URL_PATTERN = Pattern
			.compile("^/[a-z]{2,3}_[A-Z]{2}/");

	/**
	 * Ritorna il servletPath con la sua la query string eliminando, se
	 * presente, la parte relativa alla localizzazione (/XX_yy). Ad esempio:
	 * 
	 * <pre>
	 *  Input:
	 *  .../site/IT_it/games_todownload_detail.do?idcont=2007&amp;canale=&amp;propertyName=
	 *  output:
	 *  /games_todownload_detail.do?idcont=2007&amp;canale=&amp;propertyName=
	 *  Input:
	 *  .../site/IT_it/card.html
	 *  output
	 *  /card.html
	 *  input:
	 *  .../site/card.html
	 *  output
	 *  /card.html
	 * </pre>
	 * 
	 * @param request
	 * @return
	 */
	private static String getDestinationPage(HttpServletRequest request) {

		StringBuffer destination = new StringBuffer(128);

		// recupero servlet path
		String servletPath = request.getPathInfo();
		log.debug("servletPath=" + servletPath);
		// elimino la localizzazione se presente
		Matcher matcher = LOCALE_URL_PATTERN.matcher(servletPath);
		if (matcher.find()) {
			// mantiene lo slash
			destination.append(servletPath.substring(matcher.end() - 1));
		} else {
			destination.append(servletPath);
		}

		// destination.append(request.getRequestURI());

		// aggiungo l'eventuale query string
		String query = request.getQueryString();
		if (StringUtils.hasText(query)) {
			destination.append("?").append(query);
		}
		log.debug("destination=" + destination);

		return destination.toString();
	}

	// //////////////////// GETTER/SETTER ///////////////////////////////

	public String getLocaleChangeUrl() {
		return localeChangeUrl;
	}

	public void setLocaleChangeUrl(String localeChangeUrl) {
		this.localeChangeUrl = localeChangeUrl;
	}
}
