package com.alura.forumhub.infrasctructure.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;

@RestControllerAdvice
public class ErrorHandler
{
	// Error 400: Bad Request (Server unable to process request by client)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<List<ValidationError>> error400(MethodArgumentNotValidException exception) {
		List<FieldError> errors = exception.getFieldErrors();
		return ResponseEntity.badRequest().body(errors.stream().map(ValidationError::new).toList());
	}
	
	// Error 400: Bad Request (Duplicate entry)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<String> errorDuplicate() {
		return ResponseEntity.badRequest().body("Duplicate entry not allowed.");
	}
	
	// Error 404: Not Found
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<String> error404() {
		return ResponseEntity.notFound().build();
	}
	
	private record ValidationError(String field, String message) {
		public ValidationError(FieldError error) {
			this(error.getField(), error.getDefaultMessage());
		}
	}
}