package com.abdelaziz.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.annotation.CreatedDate;

import com.abdelaziz.consts.SecurityConsts;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "app_user")
public class User {

	@Id
	@GeneratedValue
	private Long id;

	@NotNull
	@Pattern(regexp = SecurityConsts.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	@Column(length = 50, unique = true, nullable = false)
	private String login;

	@NotNull
	@Size(min = 60, max = 60)
	@Column(length = 60, nullable = false)
	private String password;

	@Size(max = 50)
	@Column(length = 50)
	private String firstName;

	@Size(max = 50)
	@Column(length = 50)
	private String lastName;

	@Email
	@Size(min = 5, max = 254)
	@Column(length = 254, unique = true)
	private String email;

	@NotNull
	@Column(nullable = false)
	private boolean activated = false;

	@Size(max = 20)
	@Column(length = 20)
	private String activationKey;

	@Size(max = 20)
	@Column(length = 20)
	private String resetKey;

	private Instant resetDate = null;

	@ManyToMany
	@JoinTable(joinColumns = { @JoinColumn(referencedColumnName = "id") }, inverseJoinColumns = { @JoinColumn(referencedColumnName = "name") })
	@BatchSize(size = 20)
	private Set<Authority> authorities = new HashSet<>();

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdDate = Instant.now();
	
	// Lowercase the login before saving it in database
	public void setLogin(String login) {
		this.login = StringUtils.lowerCase(login, Locale.ENGLISH);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		User user = (User) o;
		return !(user.getId() == null || getId() == null) && Objects.equals(getId(), user.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}
}
