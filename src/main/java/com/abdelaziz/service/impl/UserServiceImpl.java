package com.abdelaziz.service.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.consts.SecurityConsts;
import com.abdelaziz.dto.UserDto;
import com.abdelaziz.dto.UserUpdateDto;
import com.abdelaziz.entity.Authority;
import com.abdelaziz.entity.User;
import com.abdelaziz.exception.InvalidPasswordException;
import com.abdelaziz.repository.AuthorityRepository;
import com.abdelaziz.repository.UserRepository;
import com.abdelaziz.service.MailService;
import com.abdelaziz.service.UserService;
import com.abdelaziz.util.RandomUtil;
import com.abdelaziz.util.SecurityUtil;

@Loggable(layer = ApplicationLayer.SERVICE_LAYER)
@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuthorityRepository authorityRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private RandomUtil randomUtil;

	@Autowired
	private SecurityUtil securityUtil;
	
	@Autowired
	private MailService mailService;
	
	private ModelMapper modelMapper = new ModelMapper();

	@Override
	public Optional<UserDto> activateRegistration(String key) {
		return userRepository.findOneByActivationKey(key).map(user -> {
			user.setActivated(true);
			user.setActivationKey(null);
			return modelMapper.map(user, UserDto.class);
		});
	}

	@Override
	public Optional<UserDto> completePasswordReset(String newPassword, String key) {
		return userRepository.findOneByResetKey(key)
			.filter(user -> user.getResetDate()
					.isAfter(Instant.now().minusSeconds(86400)))
			.map(user -> {
				user.setPassword(passwordEncoder.encode(newPassword));
				user.setResetKey(null);
				user.setResetDate(null);
				return modelMapper.map(user, UserDto.class);
			});
	}

	@Override
	@Transactional(rollbackFor = {MessagingException.class})
	public Optional<UserDto> requestPasswordReset(String mail) throws MessagingException {
		Optional<User> optionalUser = userRepository.findOneByEmailIgnoreCase(mail).filter(User::isActivated);
		
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setResetKey(randomUtil.generateResetKey());
			user.setResetDate(Instant.now());
			mailService.sendPasswordResetMail(user);
			return Optional.of(modelMapper.map(user, UserDto.class));
		}
		return Optional.empty();
	}

	@Override
	@Transactional(rollbackFor = {MessagingException.class})
    public UserDto registerUser(UserDto userDTO, String password) throws MessagingException {
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin());
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail());
        newUser.setActivated(false);
        newUser.setActivationKey(randomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(SecurityConsts.ROLE_USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);
        User createdUser = userRepository.save(newUser);
        mailService.sendActivationEmail(createdUser);
        return modelMapper.map(createdUser, UserDto.class);
    }
	
	@Override
	@Transactional(rollbackFor = {MessagingException.class})
	public UserDto createUser(UserDto userDto) throws MessagingException {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        if (userDto.getAuthorities() != null && !userDto.getAuthorities().isEmpty()){
            Set<Authority> authorities = userDto.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        } else {
        	user.setAuthorities(new HashSet<Authority>());
        	authorityRepository.findById(SecurityConsts.ROLE_USER).ifPresent(user.getAuthorities()::add);
        }
        String encryptedPassword = passwordEncoder.encode(randomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(randomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        User newUser = userRepository.save(user);
        mailService.sendCreationEmail(newUser);
        return modelMapper.map(user, UserDto.class);
    }

	@Override
	public Optional<UserDto> updateUser(String userLogin, String firstName, String lastName, String email) {
        Optional<User> updatedUser = securityUtil.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        
        if (updatedUser.isPresent()) {
        	User user = updatedUser.get();
        	user.setLogin(userLogin);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            return Optional.of(modelMapper.map(user, UserDto.class));
        }
        return Optional.empty();
	}

	@Override
	public Optional<UserDto> updateUser(UserDto userDTO) {
        return Optional.of(userRepository.findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                if (userDTO.getAuthorities() != null) {
                    Set<Authority> managedAuthorities = user.getAuthorities();
                    managedAuthorities.clear();
                    userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                }
                return modelMapper.map(user, UserDto.class);
            });
        }

	@Override
	public Boolean deleteUser(String login) {
        Optional<User> userToDelete = userRepository.findOneByLogin(login);
        if (userToDelete.isPresent()) {
        	userRepository.delete(userToDelete.get());
        	return Boolean.TRUE;
        } else {
        	return Boolean.FALSE;
        }
    }

	@Override
	public Optional<UserDto> changePassword(String currentClearTextPassword, String newPassword) {
        Optional<User> userToUpdate = securityUtil.getCurrentUserLogin().flatMap(userRepository::findOneByLogin);
        
        if(userToUpdate.isPresent()) {
        	User user = userToUpdate.get();
            String currentEncryptedPassword = user.getPassword();
            if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                throw new InvalidPasswordException();
            }
            String encryptedPassword = passwordEncoder.encode(newPassword);
            user.setPassword(encryptedPassword);
            return Optional.of(modelMapper.map(user, UserDto.class));
        }
        return Optional.empty();
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserByLogin(String login) {
		return userRepository.findOneByLogin(login.toLowerCase()).map(user -> modelMapper.map(user, UserDto.class));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserByEmail(String email) {
		return userRepository.findOneByEmailIgnoreCase(email).map(user -> modelMapper.map(user, UserDto.class));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserById(Long id) {
		return userRepository.findById(id).map(user -> modelMapper.map(user, UserDto.class));
	}
	
	@Override
	@Transactional(readOnly = true)
	public Page<UserDto> getAllManagedUsers(Pageable pageable) {
		return userRepository.findAllByLoginNot(pageable, SecurityConsts.ANONYMOUS_USER).map(user -> modelMapper.map(user, UserDto.class));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserWithAuthoritiesByLogin(String login) {
		return userRepository.findOneWithAuthoritiesByLogin(login).map(user -> modelMapper.map(user, UserDto.class));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserWithAuthorities(Long id) {
		return userRepository.findOneWithAuthoritiesById(id).map(user -> modelMapper.map(user, UserDto.class));
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<UserDto> getUserWithAuthorities() {
		return securityUtil.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin).map(user -> modelMapper.map(user, UserDto.class));
	}

    /**
     * Not activated users should be automatically deleted after 3 days.
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
	@Override
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            userRepository.delete(user);
        }
    }

	@Override
	public List<String> getAuthorities() {
		return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
	}

}
