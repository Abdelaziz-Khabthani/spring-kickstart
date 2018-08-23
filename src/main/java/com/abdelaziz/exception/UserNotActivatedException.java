package com.abdelaziz.exception;

public class UserNotActivatedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public UserNotActivatedException() {
		super("User is not activated");
	}
	
	public UserNotActivatedException(String message) {
		super(message);
	}
}
