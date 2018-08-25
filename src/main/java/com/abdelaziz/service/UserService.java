package com.abdelaziz.service;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abdelaziz.dto.UserDto;
import com.abdelaziz.dto.UserUpdateDto;

public interface UserService {
	public Optional<UserDto> activateRegistration(String key);

	public Optional<UserDto> completePasswordReset(String newPassword, String key);

	public Optional<UserDto> requestPasswordReset(String mail) throws MessagingException;

	public UserDto createUser(UserDto userDto) throws MessagingException;

	public Optional<UserDto> updateUser(String userLogin, String firstName, String lastName, String email);

	public Optional<UserDto> updateUser(UserDto userDto);

	public Boolean deleteUser(String login);

	public Optional<UserDto> changePassword(String currentClearTextPassword, String newPassword);

	public Page<UserDto> getAllManagedUsers(Pageable pageable);

	public Optional<UserDto> getUserWithAuthoritiesByLogin(String login);

	public Optional<UserDto> getUserWithAuthorities(Long id);

	public Optional<UserDto> getUserWithAuthorities();

	public void removeNotActivatedUsers();

	public List<String> getAuthorities();

	public Optional<UserDto> getUserByLogin(String login);

	public Optional<UserDto> getUserByEmail(String email);

	public Optional<UserDto> getUserById(Long id);

	public UserDto registerUser(UserDto userDTO, String password) throws MessagingException;
}
