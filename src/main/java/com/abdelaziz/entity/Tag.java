package com.abdelaziz.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Tag {
	
	@Id
	@NotBlank (message = "{Tag.Notlabel.NotBlank}")
	private String label;
	
	@ManyToMany(fetch = FetchType.LAZY)
	private List<Quote> quotes;

	public Tag(@NotBlank String label) {
		super();
		this.label = label;
	}
}
