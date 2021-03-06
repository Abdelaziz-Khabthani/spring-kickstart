package com.abdelaziz.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import com.neovisionaries.i18n.CountryCode;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Author {

	@GeneratedValue
	@Id
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String description;

	@Past
	@NotNull
	private LocalDateTime dateOfBirth;

	@Enumerated(EnumType.STRING)
	@NotNull
	private CountryCode countryOfBirth;

	public Author(@NotBlank String name, @NotBlank String description, @Past @NotNull LocalDateTime dateOfBirth, @NotNull CountryCode countryOfBirth) {
		super();
		this.name = name;
		this.description = description;
		this.dateOfBirth = dateOfBirth;
		this.countryOfBirth = countryOfBirth;
	}
}