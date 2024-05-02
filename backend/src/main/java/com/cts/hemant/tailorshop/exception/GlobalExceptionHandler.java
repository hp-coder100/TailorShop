package com.cts.hemant.tailorshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	
	
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ExceptionModel>  handleCustomeException(HttpServletRequest req, ResourceNotFoundException ex) {
		
		log.error(ex.getMessage(), ex);
		ExceptionModel exceptionModel = new  ExceptionModel(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),
				req.getRequestURL().toString(), req.getMethod());
		return new ResponseEntity<ExceptionModel>(exceptionModel, HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ExceptionModel> handleBadException(HttpServletRequest req, BadCredentialsException ex) {
		
		log.error(ex.getMessage(), ex);
		ExceptionModel exceptionModel = new  ExceptionModel(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),
				req.getRequestURL().toString(), req.getMethod());
		return new ResponseEntity<ExceptionModel>(exceptionModel, HttpStatus.BAD_REQUEST);
	}
	
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionModel> handleAllException(HttpServletRequest req, Exception ex) {
		
		log.error(ex.getMessage(), ex);
		ExceptionModel exceptionModel = new  ExceptionModel(HttpStatus.BAD_REQUEST.toString(), ex.getMessage(),
				req.getRequestURL().toString(), req.getMethod());
		return new ResponseEntity<ExceptionModel>(exceptionModel, HttpStatus.BAD_REQUEST);
	}
	

}
	