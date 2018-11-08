package com.isochrones.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class IsochroneExceptionHandler {

	@ExceptionHandler
	public ResponseEntity<com.isochrones.model.Error> handleException(ServletRequestBindingException e){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new com.isochrones.model.Error(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
	}
	
	@ExceptionHandler
	public ResponseEntity<com.isochrones.model.Error> handleException(Exception e){
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new com.isochrones.model.Error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
	}
	
}
