package com.abdelaziz.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.abdelaziz.service.TokenProviderService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private TokenProviderService tokenProvider;
	
	@Autowired
	private CorsFilter corsFilter;
	
	@Autowired
	private AuthenticationEntryPoint restAuthenticationFailureHandler;
	
	@Override
	protected void configure(@Autowired AuthenticationManagerBuilder auth) throws Exception {
		auth
			.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
    @Override
    public void configure(WebSecurity web) throws Exception {
        web
        	.ignoring()
			.antMatchers("/doc/**")
			.antMatchers("/swagger-resources/**")
			.antMatchers("/v2/api-docs")
			.antMatchers("/swagger-ui.html");
}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
        http
	        .csrf()
	        	.disable()
        	.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
	        .headers()
		        .frameOptions()
		        .disable()
	    .and()
	    	.formLogin()
	    		.disable()
	        .sessionManagement()
	        	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	    .and()
	        .authorizeRequests()
	        .antMatchers("/api/register").permitAll()
	        .antMatchers("/api/activate").permitAll()
	        .antMatchers("/api/authenticate").permitAll()
	        .antMatchers("/api/account/reset-password/init").permitAll()
	        .antMatchers("/api/account/reset-password/finish").permitAll()
			.anyRequest().authenticated()
		.and()
			.exceptionHandling()
				.authenticationEntryPoint(restAuthenticationFailureHandler)
        .and()
        	.apply(securityConfigurerAdapter());
	}
	
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    private JWTConfigurer securityConfigurerAdapter() {
        return new JWTConfigurer(tokenProvider);
    }
}
