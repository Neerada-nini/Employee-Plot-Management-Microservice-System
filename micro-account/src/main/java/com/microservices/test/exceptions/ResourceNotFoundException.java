package com.microservices.test.exceptions;



public class ResourceNotFoundException extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException() {
		super("RESOURCE NOT FOUND EXCEPTION");
	}
	
	public ResourceNotFoundException(String customMessage) {
		super(customMessage);
	}
}
