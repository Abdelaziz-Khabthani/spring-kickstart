package com.abdelaziz.exception;

public class EmailNotFoundException extends RuntimeException {

	public EmailNotFoundException() {
		super("Email does not exists");
	}

	public EmailNotFoundException(String message) {
		super(message);
	}

}
