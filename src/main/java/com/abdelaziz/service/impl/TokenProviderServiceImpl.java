package com.abdelaziz.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.abdelaziz.annotations.Loggable;
import com.abdelaziz.consts.ApplicationLayer;
import com.abdelaziz.dto.JwtTokenDto;
import com.abdelaziz.service.TokenProviderService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Setter;

@Service
@Loggable(layer = ApplicationLayer.SERVICE_LAYER)
@ConfigurationProperties(prefix = "custom.security.jwt")
public class TokenProviderServiceImpl implements TokenProviderService{

	@Setter
	private String secret;

	@Setter
	private Long tokenValidityInSeconds;

	@Setter
	private Long tokenValidityInSecondsForRememberMe;

	private static final String AUTHORITIES_KEY = "auth";

	private final Base64.Encoder encoder = Base64.getEncoder();

	private String secretKey;

	private long tokenValidityInMilliseconds;

	@PostConstruct
	public void init() {
		this.secretKey = encoder.encodeToString(secret.getBytes(StandardCharsets.UTF_8));
		this.tokenValidityInMilliseconds = 1000 * tokenValidityInSeconds;
	}

	@Override
	public JwtTokenDto createToken(Authentication authentication) {
		String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

		long now = (new Date()).getTime();
		Date validity = new Date(now + this.tokenValidityInMilliseconds);

		String token = Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities).signWith(SignatureAlgorithm.HS512, secretKey).setExpiration(validity).compact();
		return new JwtTokenDto(token, validity);
	}
	
	@Override
	public JwtTokenDto refreshToken(String token) {
		Authentication authentication = this.getAuthentication(token);
		return this.createToken(authentication);
	}
	
	@Override
	public Authentication getAuthentication(String token) {
		Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();

		Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new).collect(Collectors.toList());

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, token, authorities);
	}

	@Override
	public Boolean validateToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
