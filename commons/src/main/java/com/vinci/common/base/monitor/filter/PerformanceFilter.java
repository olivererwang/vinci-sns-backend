/*
 * $Id: PerformanceFilter.java 9565 2012-12-05 08:03:43Z build $ Copyright (c) 2011 Qunar.com. All Rights Reserved.
 */

package com.vinci.common.base.monitor.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import com.vinci.common.base.monitor.util.PerformanceMonitor;
import com.vinci.common.base.monitor.util.RequestStats;
import com.vinci.common.base.monitor.util.SystemTimer;

/**
 * @author sunli
 */
public class PerformanceFilter implements Filter {
    private static final String monitorPrefix = "GlobalPerformance";
    private PerformanceMonitor monitor = null;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        long start = SystemTimer.currentTimeMillis();
        try {
            chain.doFilter(request, response);
        } finally {
            long send = SystemTimer.currentTimeMillis() - start;
            monitor.markSlowrRquests(send);
            RequestStats.incrementPath(this.getMMVCpath((HttpServletRequest) request), send);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {

    }

    private String getMMVCpath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return uri.substring(request.getContextPath().length());
    }

    /*
     * (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        monitor = new PerformanceMonitor(monitorPrefix);
    }

}
