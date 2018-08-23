package com.abdelaziz.dto.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
public class ExceptionValidationDetails extends ExceptionDetails {
	private String object;
	private String field;
	private Object rejectedValue;
	private String message;
}
