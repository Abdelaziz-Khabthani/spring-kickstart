package com.abdelaziz.exception;

public class EmailAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmailAlreadyUsedException() {
		super("Email is already used");
	}

	public EmailAlreadyUsedException(String message) {
		super(message);
	}
}
