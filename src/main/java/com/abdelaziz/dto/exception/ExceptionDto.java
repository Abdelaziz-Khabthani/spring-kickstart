package com.abdelaziz.dto.exception;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class ExceptionDto {

	private HttpStatus status;

	@Setter(AccessLevel.NONE)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private Date datetime = new Date();

	@JsonInclude(Include.NON_EMPTY)
	private String message;

	private String debugMesssage;

	@JsonInclude(Include.NON_EMPTY)
	private List<ExceptionDetails> details;

	public ExceptionDto(HttpStatus status, String message, Exception exception) {
		super();
		this.status = status;
		this.message = message;
		this.debugMesssage = exception.getLocalizedMessage();
	}

	public ExceptionDto(HttpStatus status, Exception exception) {
		super();
		this.status = status;
		this.debugMesssage = exception.getLocalizedMessage();
	}

	public ExceptionDto(HttpStatus status, String message, Exception exception, List<ExceptionDetails> details) {
		super();
		this.status = status;
		this.message = message;
		this.debugMesssage = exception.getLocalizedMessage();
		this.details = details;
	}
}
