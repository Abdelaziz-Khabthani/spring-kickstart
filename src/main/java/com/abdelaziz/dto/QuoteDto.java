package com.abdelaziz.dto;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDto {

	private Long id;

	@NotBlank(message = "{QuoteDto.content.NotBlank}")
	private String content;
}
