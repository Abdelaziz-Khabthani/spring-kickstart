package com.abdelaziz.exception;

public class InvalidPasswordException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException() {
		super("Incorrect password");
	}
	
	public InvalidPasswordException(String message) {
		super(message);
	}
}
