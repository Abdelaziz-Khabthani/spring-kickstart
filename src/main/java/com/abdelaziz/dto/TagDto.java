package com.abdelaziz.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TagDto {

	@NotBlank(message = "{TagDto.Notlabel.NotBlank}")
	private String label;
}
