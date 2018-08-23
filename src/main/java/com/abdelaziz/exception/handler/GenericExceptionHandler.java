package com.abdelaziz.exception.handler;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.dto.exception.ExceptionDetails;
import com.abdelaziz.dto.exception.ExceptionDto;
import com.abdelaziz.dto.exception.ExceptionValidationDetails;
import com.abdelaziz.util.LocalsUtil;

@Loggable(layer = ApplicationLayer.EXCEPTION_HANDLING_LAYER)
@RestControllerAdvice
public class GenericExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private LocalsUtil localsUtil;

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		ExceptionDto exceptionDto = new ExceptionDto(status, ex);
		return new ResponseEntity<Object>(exceptionDto, headers, status);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<ExceptionDetails> exceptionValidationDetails = new ArrayList<ExceptionDetails>();
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			exceptionValidationDetails.add(new ExceptionValidationDetails(fieldError.getObjectName(), fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
		}
		ExceptionDto exceptionDto = new ExceptionDto(status, localsUtil.getSimpleLocalizedMessage("http.error.validation"), ex, exceptionValidationDetails);
		return new ResponseEntity<Object>(exceptionDto, headers, status);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ExceptionDto> handleFallbackException(Exception ex, WebRequest request) {
		ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.INTERNAL_SERVER_ERROR, localsUtil.getSimpleLocalizedMessage("http.error.server"), ex);
		return new ResponseEntity<ExceptionDto>(exceptionDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
