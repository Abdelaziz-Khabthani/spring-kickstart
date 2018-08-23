package com.abdelaziz.dto;

import java.time.Instant;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.abdelaziz.consts.SecurityConsts;
import com.abdelaziz.entity.Authority;
import com.abdelaziz.entity.User;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDto {
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

	private Instant createdDate;
	
	private boolean activated = false;

	private Set<String> authorities;
}
