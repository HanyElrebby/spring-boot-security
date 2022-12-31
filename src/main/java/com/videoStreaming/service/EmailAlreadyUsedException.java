package com.videoStreaming.service;


public class EmailAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyUsedException() {
		super("Email already used");
	}
	
}
