package com.abdelaziz.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.abdelaziz.consts.SecurityConsts;

import lombok.Data;

@Data
public class LoginDto {
	@NotNull
	@Size(min = 1, max = 50)
	private String username;

	@NotNull
	@Size(min = SecurityConsts.PASSWORD_MIN_LENGTH, max = SecurityConsts.PASSWORD_MAX_LENGTH)
	private String password;

	private Boolean rememberMe;
}
