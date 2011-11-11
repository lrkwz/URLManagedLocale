package com.github.lrkwz.web;

import javax.servlet.ServletRequest;

/**
 * A <code>PortResolver</code> determines the port a web request was received
 * on.
 * 
 * <P>
 * This interface is necessary because
 * <code>ServletRequest.getServerPort()</code> may not return the correct port
 * in certain circumstances. For example, if the browser does not construct the
 * URL correctly after a redirect.
 * </p>
 * 
 * @author Ben Alex
 */
public interface PortResolver {
	// ~ Methods
	// ========================================================================================================

	/**
	 * Indicates the port the <code>ServletRequest</code> was received on.
	 * 
	 * @param request
	 *            that the method should lookup the port for
	 * 
	 * @return the port the request was received on
	 */
	int getServerPort(ServletRequest request);
}
