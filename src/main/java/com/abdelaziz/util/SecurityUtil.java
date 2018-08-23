package com.abdelaziz.util;

import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.consts.SecurityConsts;

@Loggable(layer = ApplicationLayer.UTILITY_LAYER)
@Component
public class SecurityUtil {

	public boolean checkPasswordLength(String password) {
		return !StringUtils.isEmpty(password) && password.length() >= SecurityConsts.PASSWORD_MIN_LENGTH && password.length() <= SecurityConsts.PASSWORD_MAX_LENGTH;
	}

	/**
	 * Get the login of the current user.
	 *
	 * @return the login of the current user
	 */
	public Optional<String> getCurrentUserLogin() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
			if (authentication.getPrincipal() instanceof UserDetails) {
				UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
				return springSecurityUser.getUsername();
			} else if (authentication.getPrincipal() instanceof String) {
				return (String) authentication.getPrincipal();
			}
			return null;
		});
	}

	/**
	 * Get the JWT of the current user.
	 *
	 * @return the JWT of the current user
	 */
	public Optional<String> getCurrentUserJWT() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication()).filter(authentication -> authentication.getCredentials() instanceof String).map(authentication -> (String) authentication.getCredentials());
	}

	/**
	 * Check if a user is authenticated.
	 *
	 * @return true if the user is authenticated, false otherwise
	 */
	public boolean isAuthenticated() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(SecurityConsts.ROLE_ANONYMOUS))).orElse(false);
	}

	/**
	 * If the current user has a specific authority (security role).
	 * <p>
	 * The name of this method comes from the isUserInRole() method in the Servlet
	 * API
	 *
	 * @param authority the authority to check
	 * @return true if the current user has the authority, false otherwise
	 */
	public boolean isCurrentUserInRole(String authority) {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority))).orElse(false);
	}
}
