package com.ljy.chat.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.chat.security.filter.JwtAuthenticationFilter;
import com.ljy.chat.security.filter.JwtAuthorizationFilter;
import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.LoginSuccessHandler;
import com.ljy.chat.security.jwt.impl.JwtLoginSuccessHandler;
import com.ljy.chat.security.jwt.util.HttpParser;
import com.ljy.chat.user.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserService userService;

	@Autowired
	private JwtStore jdbcStore;

	private final HttpParser httpParser;
	private final CorsFilter filter;
	private final String ROLE_USER = "hasRole('ROLE_USER')";
	private final String ROLE_MANAGER = "hasRole('ROLE_MANAGER')";
	private final String ROLE_ADMIN = "hasRole('ROLE_ADMIN')";

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().addFilter(filter).formLogin()
			.disable().logout().disable()
			.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
			.addFilter(new JwtAuthorizationFilter(this.authenticationManager(), userService, jdbcStore, httpParser))
			.httpBasic().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/api/user").permitAll()
			.antMatchers("/api/user/**").access(allRole()).antMatchers("/api/manager/**").access(managerRole())
			.antMatchers("/api/admin/**").access(adminRole()).antMatchers("/api/remove-token", "/api/refresh-token")
			.authenticated().antMatchers("/api/chating").authenticated().anyRequest().permitAll();
	}

	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new JwtLoginSuccessHandler(jdbcStore);
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public JwtAuthenticationFilter customAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter customAuthenticationFilter = new JwtAuthenticationFilter(super.authenticationManager(),
			new ObjectMapper(), loginSuccessHandler());
		customAuthenticationFilter.setFilterProcessesUrl("/api/login");
		return customAuthenticationFilter;
	}

	private String adminRole() {
		return ROLE_ADMIN;
	}

	private String managerRole() {
		return ROLE_MANAGER + " or " + ROLE_ADMIN;
	}

	private String allRole() {
		return ROLE_USER + " or " + ROLE_MANAGER + " or " + ROLE_ADMIN;
	}
}
