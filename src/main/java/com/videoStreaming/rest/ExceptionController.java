package com.videoStreaming.rest;

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import com.videoStreaming.rest.error.ErrorResponse;
import com.videoStreaming.rest.error.InvalidPasswordException;
import com.videoStreaming.security.UserNotActivatedException;
import com.videoStreaming.service.EmailAlreadyUsedException;
import com.videoStreaming.service.UserNotFoundException;

@ControllerAdvice
public class ExceptionController {
	
	@ExceptionHandler(value = EmailAlreadyUsedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleEmailAlreadyUsed(EmailAlreadyUsedException exception, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(true)),
				HttpStatus.BAD_REQUEST
			);
	}
	
	@ExceptionHandler(value = InvalidPasswordException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleInvalidPassword(InvalidPasswordException exception, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(false)),
				HttpStatus.BAD_REQUEST
			);
	}
	
	@ExceptionHandler(value = UserNotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException exception, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(true)),
				HttpStatus.BAD_REQUEST
			);
	}
	
	@ExceptionHandler(value = UserNotActivatedException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleUserNotActivated(UserNotActivatedException exception, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(true)),
				HttpStatus.BAD_REQUEST
			);
	}
	

	/*@ExceptionHandler(value = UsernameNotFoundException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public ResponseEntity<ErrorResponse> handleUsernameNotFound(UsernameNotFoundException exception, WebRequest request) {
		return new ResponseEntity<ErrorResponse>(
				new ErrorResponse(HttpStatus.BAD_REQUEST.value(), new Date(), exception.getMessage(), request.getDescription(true)),
				HttpStatus.BAD_REQUEST
			);
	}*/

}
