package com.abdelaziz.exception;

public class LoginAlreadyUsedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LoginAlreadyUsedException() {
		super("Login already exists");
	}

	public LoginAlreadyUsedException(String message) {
		super(message);
	}
}
