package com.github.lrkwz.web;

/**
 * <code>PortMapper</code> implementations provide callers with information
 * about which HTTP ports are associated with which HTTPS ports on the system,
 * and vice versa.
 *
 * @author Ben Alex
 */
public interface PortMapper {
    //~ Methods ========================================================================================================

    /**
     * Locates the HTTP port associated with the specified HTTPS port.<P>Returns <code>null</code> if unknown.</p>
     *
     * @param httpsPort
     *
     * @return the HTTP port or <code>null</code> if unknown
     */
    Integer lookupHttpPort(Integer httpsPort);

    /**
     * Locates the HTTPS port associated with the specified HTTP port.<P>Returns <code>null</code> if unknown.</p>
     *
     * @param httpPort
     *
     * @return the HTTPS port or <code>null</code> if unknown
     */
    Integer lookupHttpsPort(Integer httpPort);
}
