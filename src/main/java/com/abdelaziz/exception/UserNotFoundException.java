package com.abdelaziz.exception;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {
		super("User does not exists");
	}

	public UserNotFoundException(String message) {
		super(message);
	}
}
