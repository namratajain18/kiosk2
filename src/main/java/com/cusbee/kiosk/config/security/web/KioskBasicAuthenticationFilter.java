package com.cusbee.kiosk.config.security.web;

import com.cusbee.kiosk.config.security.web.DefaultFilter;
import com.cusbee.kiosk.config.security.web.MutableHttpServletRequest;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.crypto.codec.Base64;

public class KioskBasicAuthenticationFilter implements DefaultFilter {
	private static final String DEFAULT_AUTH = new String(Base64.encode("KIOSK:KIOSK".getBytes()));

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest((HttpServletRequest) request);
		String authorization = ((HttpServletRequest) request).getHeader("Authorization");
		if (authorization == null || authorization.isEmpty()) {
			mutableRequest.putHeader("Authorization", String.join(" ", new CharSequence[]{"Basic", DEFAULT_AUTH}));
		}

		chain.doFilter(mutableRequest, response);
	}
}