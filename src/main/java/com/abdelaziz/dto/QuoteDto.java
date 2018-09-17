package com.abdelaziz.dto;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuoteDto {

	private Long id;

	@NotBlank
	private String content;
	
	private LocalDateTime creationDate;
	
	@NotEmpty
	private Set<String> tags;
	
	private AuthorDto author;
}
