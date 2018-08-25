package com.abdelaziz.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representing a password change required data - current and new password.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
	@NotNull
    private String currentPassword;
	@NotNull
	private String newPassword;
}
