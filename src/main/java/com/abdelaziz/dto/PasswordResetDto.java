package com.abdelaziz.dto;

import javax.validation.constraints.Email;

import lombok.Data;

@Data
public class PasswordResetDto {
	@Email
	private String email;
}
