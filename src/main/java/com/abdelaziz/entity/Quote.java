package com.abdelaziz.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Entity
public class Quote {

	@GeneratedValue
	@Id
	private Long id;

	@NotBlank
	private String content;

	@CreatedDate
	@Setter(AccessLevel.NONE)
	private LocalDateTime creationDate;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	private Author author;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "quotes")
	private List<Tag> tags;

	public Quote(@NotBlank String content) {
		super();
		this.content = content;
	}
}