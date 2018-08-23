package com.abdelaziz.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// Swagger-ui static resource handler, because we are disabling static resource
		// handling in application.properties
		registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		// Changing swagger url mapping with redirects, workaround
		registry.addRedirectViewController("/doc/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true);
		registry.addRedirectViewController("/doc/configuration/ui", "/swagger-resources/configuration/ui");
		registry.addRedirectViewController("/doc/configuration/security", "/swagger-resources/configuration/security");
		registry.addRedirectViewController("/doc/swagger-resources", "/swagger-resources");
		registry.addRedirectViewController("/doc", "/swagger-ui.html");
		registry.addRedirectViewController("/doc/", "/swagger-ui.html");
	}
}
