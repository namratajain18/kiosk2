package com.cusbee.kiosk.config.security.web;

import com.cusbee.kiosk.config.ApplicationContext;
import com.cusbee.kiosk.config.security.web.DefaultFilter;
import com.cusbee.kiosk.config.security.web.MutableHttpServletRequest;
import com.cusbee.kiosk.repository.UsersRepository;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class KioskMobileClientCredentialsFilter implements DefaultFilter {
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String userphone = request.getParameter("userphone");
		MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest((HttpServletRequest) request);
		if (userphone != null && !userphone.trim().isEmpty()) {
			ApplicationContext.getContext().map((ctx) -> {
				return (UsersRepository) ctx.getBean(UsersRepository.class);
			}).map((repository) -> {
				return repository.findByPhoneNumber(userphone);
			}).ifPresent((user) -> {
				mutableRequest.putParameter("username", user.getUsername());
				mutableRequest.putParameter("password", user.getPassword());
				mutableRequest.putParameter("grant_type", "password");
			});
		}

		chain.doFilter(mutableRequest, response);
	}
}