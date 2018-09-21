package com.cusbee.kiosk.exception;

import org.springframework.security.authentication.AccountStatusException;

public class VerifyException extends AccountStatusException {
	public VerifyException(String msg) {
		super(msg);
	}

	public VerifyException(String msg, Throwable t) {
		super(msg, t);
	}
}