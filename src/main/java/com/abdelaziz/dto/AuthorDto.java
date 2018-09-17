package com.abdelaziz.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.neovisionaries.i18n.CountryCode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String description;

	@Past
	@NotNull
	private Date dateOfBirth;
	
	@NotNull
	private CountryCode countryOfBirth;
}
