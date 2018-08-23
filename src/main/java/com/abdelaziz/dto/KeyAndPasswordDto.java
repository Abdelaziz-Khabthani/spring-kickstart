package com.abdelaziz.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyAndPasswordDto {
	private String key;
	private String newPassword;
}
