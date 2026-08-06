package com.microservices.test.exceptions;



public class DuplicateResourceException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateResourceException() {
		super("RESOURCE ALREADY EXISTS");
	}
	
	public DuplicateResourceException(String customMessage) {
		super(customMessage);
	}
}
