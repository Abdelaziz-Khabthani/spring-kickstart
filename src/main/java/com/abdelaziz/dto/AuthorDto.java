package com.abdelaziz.dto;

import java.util.Date;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

	private Long id;

	@NotBlank(message = "{AuthorDto.name.NotBlank}")
	private String name;

	@NotBlank(message = "{AuthorDto.description.NotBlank}")
	private String description;

	@Past(message = "{AuthorDto.dateOfBirth.Past}")
	@NotNull(message = "{AuthorDto.dateOfBirth.NotNull}")
	private Date dateOfBirth;
}
