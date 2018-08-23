package com.abdelaziz.dto;

import javax.validation.constraints.Size;

import com.abdelaziz.consts.SecurityConsts;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserWithPasswordDto extends UserDto {
	@Size(min = SecurityConsts.PASSWORD_MIN_LENGTH, max = SecurityConsts.PASSWORD_MAX_LENGTH)
	private String password;
}
