package com.abdelaziz.resource;

import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.config.JWTConfigurer;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.consts.SecurityConsts;
import com.abdelaziz.dto.AuthenticationDto;
import com.abdelaziz.dto.JwtTokenDto;
import com.abdelaziz.dto.KeyAndPasswordDto;
import com.abdelaziz.dto.LoginDto;
import com.abdelaziz.dto.PasswordChangeDto;
import com.abdelaziz.dto.PasswordResetDto;
import com.abdelaziz.dto.UserDto;
import com.abdelaziz.dto.UserWithPasswordDto;
import com.abdelaziz.exception.EmailAlreadyUsedException;
import com.abdelaziz.exception.EmailNotFoundException;
import com.abdelaziz.exception.InternalServerErrorException;
import com.abdelaziz.exception.InvalidPasswordException;
import com.abdelaziz.exception.LoginAlreadyUsedException;
import com.abdelaziz.exception.UserNotFoundException;
import com.abdelaziz.service.UserService;
import com.abdelaziz.service.impl.TokenProviderServiceImpl;
import com.abdelaziz.util.SecurityUtil;

@Loggable(layer = ApplicationLayer.RESOURCE_LAYER)
@RestController
public class AccountResource extends ApiRootPath{

	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityUtil securityUtil;
	
	@Autowired
    private TokenProviderServiceImpl tokenProvider;

	@Autowired
    private AuthenticationManager authenticationManager;
	
    @PostMapping("/register")
    public ResponseEntity<Void> registerAccount(@Valid @RequestBody UserWithPasswordDto userWithPasswordDto) throws MessagingException {
        if (!securityUtil.checkPasswordLength(userWithPasswordDto.getPassword())) {
            throw new InvalidPasswordException();
        }
		if (userService.getUserByLogin(userWithPasswordDto.getLogin()).isPresent()) {
			throw new LoginAlreadyUsedException();
		} else if (userService.getUserByEmail(userWithPasswordDto.getEmail()).isPresent()) {
			throw new EmailAlreadyUsedException();
		} else {
			UserDto newUser = userService.registerUser(userWithPasswordDto, userWithPasswordDto.getPassword());
			UriComponents uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newUser.getId());
			return ResponseEntity.created(uri.toUri()).build();
		}
    }
	
    @GetMapping("/activate")
    public ResponseEntity<Void> activateAccount(@RequestParam(value = "key") String key) {
        Optional<UserDto> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
        
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/authenticate")
    public AuthenticationDto isAuthenticated(HttpServletRequest request) {
    	String login = request.getRemoteUser();
        if(login == null) {
        	return new AuthenticationDto(Boolean.FALSE, null);
        } else {
        	return new AuthenticationDto(Boolean.TRUE, login);
        }
    }
    
    @PostMapping("/authenticate")
    public JwtTokenDto authorize(@Valid @RequestBody LoginDto loginDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginDto.getRememberMe() == null) ? false : loginDto.getRememberMe();
        JwtTokenDto jwtTokenDto = tokenProvider.createToken(authentication, rememberMe);
        return jwtTokenDto;
    }
    
    @GetMapping("/refresh-authentication")
    public JwtTokenDto authorize(HttpServletRequest request) {
    	String bearerToken = request.getHeader(SecurityConsts.AUTHORIZATION_HEADER);
    	String token = bearerToken.substring(7, bearerToken.length());
    	return tokenProvider.refreshToken(token);
    }
    
    @GetMapping("/account")
    public UserDto getAccount() {
        return userService.getUserWithAuthorities()
            .orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
    }
    
    @PutMapping("/account")
    public ResponseEntity<Void> updateAccount(@Valid @RequestBody UserDto userDto) {
        final String userLogin = securityUtil.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<UserDto> user = userService.getUserByLogin(userLogin);
        
		if (!user.isPresent()) {
			throw new UserNotFoundException();
		}
		UserDto existingUser = user.get();
		
		Optional<UserDto> existingUserByEmail = userService.getUserByEmail(userDto.getEmail());
		if (existingUserByEmail.isPresent() && (!existingUserByEmail.get().getId().equals(existingUser.getId()))) {
			throw new EmailAlreadyUsedException();
		}

		Optional<UserDto> existingUserByLogin = userService.getUserByLogin(userDto.getLogin());
		if (existingUserByLogin.isPresent() && (!existingUserByLogin.get().getId().equals(existingUser.getId()))) {
			throw new LoginAlreadyUsedException();
		}

        Optional<UserDto> updatedUser = userService.updateUser(userDto.getLogin(), userDto.getFirstName(), userDto.getLastName(), userDto.getEmail());
        
		if (!updatedUser.isPresent()) {
			throw new InternalServerErrorException("Something went wrong when updating the user");
		}
		
		return ResponseEntity.noContent().build();
   }
    
    @PostMapping(path = "/account/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDto passwordChangeDto) {
        if (!securityUtil.checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<UserDto> updatedUser = userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        
		if (!updatedUser.isPresent()) {
			throw new InternalServerErrorException("Something went wrong when changing the password");
		}
		
		return ResponseEntity.noContent().build();
    }
    
    @PostMapping(path = "/account/reset-password/init")
    public ResponseEntity<Void> requestPasswordReset(@RequestBody PasswordResetDto passwordResetDto) throws MessagingException {
    	userService.requestPasswordReset(passwordResetDto.getEmail()).orElseThrow(EmailNotFoundException::new);
    	return ResponseEntity.noContent().build();
    }
    
    @PostMapping(path = "/account/reset-password/finish")
    public ResponseEntity<Void> finishPasswordReset(@RequestBody KeyAndPasswordDto keyAndPasswordDto) {
        if (!securityUtil.checkPasswordLength(keyAndPasswordDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<UserDto> user = userService.completePasswordReset(keyAndPasswordDto.getNewPassword(), keyAndPasswordDto.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
        
        return ResponseEntity.noContent().build();
    }
}
