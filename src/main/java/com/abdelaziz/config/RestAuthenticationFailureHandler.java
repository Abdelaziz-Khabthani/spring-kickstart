package com.abdelaziz.config;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.abdelaziz.dto.exception.ExceptionDto;
import com.abdelaziz.util.LocalsUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class RestAuthenticationFailureHandler implements AuthenticationEntryPoint {

	@Autowired
	LocalsUtil localsUtil;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		ExceptionDto exceptionDto = new ExceptionDto(HttpStatus.FORBIDDEN, localsUtil.getSimpleLocalizedMessage("http.error.authentication"), authException);

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		OutputStream out = response.getOutputStream();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.writeValue(out, exceptionDto);
		out.flush();
	}

}
