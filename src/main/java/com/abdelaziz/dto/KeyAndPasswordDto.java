package com.abdelaziz.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndPasswordDto {
	@NotNull
	private String key;
	@NotNull
	private String newPassword;
}
