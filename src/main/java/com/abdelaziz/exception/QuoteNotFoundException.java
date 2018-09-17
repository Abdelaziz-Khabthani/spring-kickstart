package com.abdelaziz.exception;

public class QuoteNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public QuoteNotFoundException() {
		super("Quote not found");
	}
	
	public QuoteNotFoundException(String message) {
		super(message);
	}
}
