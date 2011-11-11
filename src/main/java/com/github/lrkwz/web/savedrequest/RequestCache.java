package com.github.lrkwz.web.savedrequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Implements "saved request" logic, allowing a single request to be retrieved
 * and restarted after redirecting to an authentication mechanism.
 * 
 * @author Luke Taylor
 * @since 3.0
 */
public interface RequestCache {

	/**
	 * Caches the current request for later retrieval, once authentication has
	 * taken place. Used by <tt>ExceptionTranslationFilter</tt>.
	 * 
	 * @param request
	 *            the request to be stored
	 */
	void saveRequest(HttpServletRequest request, HttpServletResponse response);

	/**
	 * Returns the saved request, leaving it cached.
	 * 
	 * @param request
	 *            the current request
	 * @return the saved request which was previously cached, or null if there
	 *         is none.
	 */
	SavedRequest getRequest(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * Returns a wrapper around the saved request, if it matches the current
	 * request. The saved request should be removed from the cache.
	 * 
	 * @param request
	 * @param response
	 * @return the wrapped save request, if it matches the original, or null if
	 *         there is no cached request or it doesn't match.
	 */
	HttpServletRequest getMatchingRequest(HttpServletRequest request,
			HttpServletResponse response);

	/**
	 * Removes the cached request.
	 * 
	 * @param request
	 *            the current request, allowing access to the cache.
	 */
	void removeRequest(HttpServletRequest request, HttpServletResponse response);

}
