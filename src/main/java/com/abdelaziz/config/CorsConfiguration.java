package com.abdelaziz.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "custom.security.cors")
public class CorsConfiguration {
	@Setter
	private List<String> allowedOrigins;

	@Setter
	private List<String> allowedMethods;

	@Setter
	private List<String> allowedHeaders;

	@Setter
	private List<String> exposedHeaders;

	@Setter
	private Boolean allowCredentials;

	@Setter
	private Long maxAge;

	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
		config.setAllowedOrigins(allowedOrigins);
		config.setAllowedMethods(allowedMethods);
		config.setAllowedHeaders(allowedHeaders);
		config.setExposedHeaders(exposedHeaders);
		config.setAllowCredentials(allowCredentials);
		config.setMaxAge(maxAge);
		if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
			source.registerCorsConfiguration("/api/**", config);
			source.registerCorsConfiguration("/doc/**", config);
			source.registerCorsConfiguration("/swagger-resources/**", config);
			source.registerCorsConfiguration("/v2/api-docs", config);
			source.registerCorsConfiguration("/swagger-ui.html", config);
		}
		return new CorsFilter(source);
	}
}
