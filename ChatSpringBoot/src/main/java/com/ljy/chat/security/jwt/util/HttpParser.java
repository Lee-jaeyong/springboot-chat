package com.ljy.chat.security.jwt.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class HttpParser {

	private final ObjectMapper objMapper;

	public String getAccessToken(HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		return parse(request).getAccessToken();
	}

	public String getRefreshToken(HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		return parse(request).getRefreshToken();
	}

	private JwtToken parse(HttpServletRequest request) throws JsonMappingException, JsonProcessingException {
		String jwtHeader = request.getHeader("Authorization");
		if (jwtHeader == null) {
			return new JwtToken();
		}
		return objMapper.readValue(jwtHeader, JwtToken.class);
	}
}
