package com.abdelaziz.resource;

import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.consts.SecurityConsts;
import com.abdelaziz.dto.UserDto;
import com.abdelaziz.exception.EmailAlreadyUsedException;
import com.abdelaziz.exception.InternalServerErrorException;
import com.abdelaziz.exception.LoginAlreadyUsedException;
import com.abdelaziz.exception.UserNotFoundException;
import com.abdelaziz.service.UserService;
import com.abdelaziz.util.PaginationUtil;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class UserResource extends ApiRootPath {

	@Autowired
	private UserService userService;

	@Autowired
	private PaginationUtil paginationUtil;

	@PostMapping("/hello")
	@Secured(SecurityConsts.ROLE_ADMIN)
	public String hello() {
		return "hello";
	}
	
	@PostMapping("/users")
	@Secured(SecurityConsts.ROLE_ADMIN)
	public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto userDTO) throws MessagingException {
		if (userService.getUserByLogin(userDTO.getLogin()).isPresent()) {
			throw new LoginAlreadyUsedException();
		} else if (userService.getUserByEmail(userDTO.getEmail()).isPresent()) {
			throw new EmailAlreadyUsedException();
		} else {
			UserDto newUser = userService.createUser(userDTO);
			UriComponents uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId());
			return ResponseEntity.created(uri.toUri()).build();
		}
	}

	@PutMapping("/users")
	@Secured(SecurityConsts.ROLE_ADMIN)
	public ResponseEntity<Void> updateUser(@Valid @RequestBody UserDto userDto) {
		Optional<UserDto> existingUser = userService.getUserById(userDto.getId());

		if (!existingUser.isPresent()) {
			throw new UserNotFoundException();
		}

		Optional<UserDto> existingUserByEmail = userService.getUserByEmail(userDto.getEmail());
		if (existingUserByEmail.isPresent() && (!existingUserByEmail.get().getId().equals(userDto.getId()))) {
			throw new EmailAlreadyUsedException();
		}

		Optional<UserDto> existingUserByLogin = userService.getUserByLogin(userDto.getLogin());
		if (existingUserByLogin.isPresent() && (!existingUserByLogin.get().getId().equals(userDto.getId()))) {
			throw new LoginAlreadyUsedException();
		}

		Optional<UserDto> updatedUser = userService.updateUser(userDto);
		
		if (!updatedUser.isPresent()) {
			throw new InternalServerErrorException("Something went wrong when updating the user");
		}
		
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/users")
	public ResponseEntity<List<UserDto>> getAllUsers(Pageable pageable) {
		final Page<UserDto> page = userService.getAllManagedUsers(pageable);
		HttpHeaders headers = paginationUtil.generatePaginationHttpHeaders(page, "/api/users");
		return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
	}

	@GetMapping("/users/{login:" + SecurityConsts.LOGIN_REGEX + "}")
	public UserDto getUser(@PathVariable String login) {
		Optional<UserDto> user = userService.getUserWithAuthoritiesByLogin(login);
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}
		return user.get();
	}

	@DeleteMapping("/users/{login:" + SecurityConsts.LOGIN_REGEX + "}")
	@Secured(SecurityConsts.ROLE_ADMIN)
	public ResponseEntity<Void> deleteUser(@PathVariable String login) {
		Optional<UserDto> user = userService.getUserByLogin(login);
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}

		Boolean result = userService.deleteUser(login);
		if (!result) {
			throw new InternalServerErrorException("Something went wrong when deleting the user");
		}
		return ResponseEntity.noContent().build();
	}
}
