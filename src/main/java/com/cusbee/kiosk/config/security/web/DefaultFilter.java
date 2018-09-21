package com.cusbee.kiosk.config.security.web;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

public interface DefaultFilter extends Filter {
	default void init(FilterConfig filterConfig) throws ServletException {
	}

	default void destroy() {
	}
}