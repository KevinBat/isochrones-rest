package com.isochrones.exception;

public class ParamException extends Exception {

	private static final long serialVersionUID = 1L;

	public ParamException() {
		super();
	}

	public ParamException(String message, Throwable cause) {
		super(message, cause);
	}

	public ParamException(String message) {
		super(message);
	}

	public ParamException(Throwable cause) {
		super(cause);
	}
	
}
