package com.isochrones.exception;

public class BadParametersException extends Exception {

	private static final long serialVersionUID = 1L;

	public BadParametersException() {
		super();
	}

	public BadParametersException(String message, Throwable cause) {
		super(message, cause);
	}

	public BadParametersException(String message) {
		super(message);
	}

	public BadParametersException(Throwable cause) {
		super(cause);
	}
	
	

}
