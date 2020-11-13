package com.ljy.chat.config.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class UtilConfig {

	@Bean
	public ObjectMapper objMapper() {
		return new ObjectMapper();
	}
}
