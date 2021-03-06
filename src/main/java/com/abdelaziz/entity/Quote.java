package com.abdelaziz.entity;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
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
	@ManyToOne(fetch = FetchType.EAGER)
	private Author author;

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	private Set<Tag> tags;

	public Quote(@NotBlank String content, @NotNull Author author, Set<Tag> tags) {
		this.content = content;
		this.author = author;
		this.tags = tags;
	}

}