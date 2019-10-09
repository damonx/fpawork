package com.fisherpaykel.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		final String errorMessage = "Http message is not readable.";
		return buildResponseEntity(new FPApiError(HttpStatus.BAD_REQUEST, errorMessage, ex));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers,
			final HttpStatus status, final WebRequest request) {
		final String errorMessage = "Method argument is wrong.";
		return buildResponseEntity(new FPApiError(HttpStatus.BAD_REQUEST, errorMessage, ex));
	}

	@ExceptionHandler(FPBadRequestException.class)
	protected ResponseEntity<Object> handleBadRequestError(
			final FPBadRequestException ex) {
		final FPApiError apiError = new FPApiError(HttpStatus.BAD_REQUEST);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(FPServerInternalException.class)
	protected ResponseEntity<Object> handleServerInternalError(
			final FPServerInternalException ex) {
		final FPApiError apiError = new FPApiError(HttpStatus.INTERNAL_SERVER_ERROR);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	@ExceptionHandler(FPNotFoundException.class)
	protected ResponseEntity<Object> handleServerInternalError(
			final FPNotFoundException ex) {
		final FPApiError apiError = new FPApiError(HttpStatus.NOT_FOUND);
		apiError.setMessage(ex.getMessage());
		return buildResponseEntity(apiError);
	}

	private ResponseEntity<Object> buildResponseEntity(final FPApiError apiError) {
		return new ResponseEntity<>(apiError, apiError.getStatus());
	}
}
