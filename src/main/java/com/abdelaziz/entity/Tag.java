package com.abdelaziz.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Tag {

	@Id
	@NotBlank
	private String label;

	public Tag(@NotBlank String label) {
		super();
		this.label = label;
	}
}
