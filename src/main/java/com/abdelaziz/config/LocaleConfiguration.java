package com.abdelaziz.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "custom.spring.messages")
public class LocaleConfiguration {

	@Setter
	private String basename;

	@Setter
	private String ecoding;

	// Because we are disabling default resource handling
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(basename);
		messageSource.setDefaultEncoding(ecoding);
		return messageSource;
	}

	// Here we are telling hibernate validation to use the auto configured spring
	// MessageSrouce
	@Bean
	public LocalValidatorFactoryBean validator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.setValidationMessageSource(this.messageSource());
		return localValidatorFactoryBean;
	}
}
