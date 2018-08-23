package com.abdelaziz.service;

import org.springframework.security.core.Authentication;

import com.abdelaziz.dto.JwtTokenDto;

public interface TokenProviderService {

	public JwtTokenDto refreshToken(String token);

	public Authentication getAuthentication(String token);

	public Boolean validateToken(String authToken);

	public JwtTokenDto createToken(Authentication authentication, Boolean rememberMe);

}
