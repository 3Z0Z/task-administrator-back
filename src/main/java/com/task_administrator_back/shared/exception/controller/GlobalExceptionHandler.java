package com.task_administrator_back.shared.exception.controller;

import com.task_administrator_back.application.exception.ProjectNotFoundException;
import com.task_administrator_back.application.exception.TaskNotFoundException;
import com.task_administrator_back.application.exception.UnauthorizedActionException;
import com.task_administrator_back.auth.exception.*;
import com.task_administrator_back.shared.exception.response.ResponseException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(UserRegistrationException.class)
	public ResponseEntity<ResponseException> handleUserRegistrationException(UserRegistrationException e) {
		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(UnableToDecryptException.class)
	public ResponseEntity<ResponseException> handleUnableToDecryptException(UnableToDecryptException e) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(JWTTokenNotProvidedException.class)
	public ResponseEntity<ResponseException> handleJWTTokenNotProvidedException(JWTTokenNotProvidedException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ResponseException> handleUsernameNotFoundException(UsernameNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(AuthenticationFailedException.class)
	public ResponseEntity<ResponseException> handleAuthenticationFailedException(AuthenticationFailedException e) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(RefreshTokenException.class)
	public ResponseEntity<ResponseException> handleRefreshTokenException(RefreshTokenException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(ProjectNotFoundException.class)
	public ResponseEntity<ResponseException> handleProjectNotFoundException(ProjectNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(TaskNotFoundException.class)
	public ResponseEntity<ResponseException> handleTaskNotFoundException(TaskNotFoundException e) {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(UnauthorizedActionException.class)
	public ResponseEntity<ResponseException> handleUnauthorizedActionException(UnauthorizedActionException e) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseException(e.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return ResponseEntity.badRequest().body(errors);
	}
	
}
