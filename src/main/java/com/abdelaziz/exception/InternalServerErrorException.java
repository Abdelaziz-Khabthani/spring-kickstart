package com.abdelaziz.exception;

public class InternalServerErrorException extends RuntimeException {

	public InternalServerErrorException() {
		super("Enternal server error");
	}

	public InternalServerErrorException(String message) {
		super(message);
	}

}
