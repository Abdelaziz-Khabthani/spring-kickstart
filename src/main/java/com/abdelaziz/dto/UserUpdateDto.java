package com.abdelaziz.dto;

import java.time.Instant;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.abdelaziz.consts.SecurityConsts;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDto {
	@NotNull
	private Long id;

	@NotBlank
	@Pattern(regexp = SecurityConsts.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String login;

	@NotBlank
	@Size(max = 50)
	private String firstName;

	@NotBlank
	@Size(max = 50)
	private String lastName;

	@NotBlank
	@Email
	@Size(min = 5, max = 254)
	private String email;

	private Set<String> authorities;
}
