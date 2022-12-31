package com.videoStreaming.security;

import org.springframework.security.core.AuthenticationException;

public class UserNotActivatedException extends AuthenticationException {

	private static final long serialVersionUID = 1L;

	public UserNotActivatedException(String msg, Throwable cause) {
		super(msg, cause);
	}

	public UserNotActivatedException(String msg) {
		super(msg);
	}

}
