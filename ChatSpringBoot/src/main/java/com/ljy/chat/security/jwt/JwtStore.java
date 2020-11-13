package com.ljy.chat.security.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.ljy.chat.security.jwt.util.JwtToken;

public interface JwtStore {
	public JwtToken alreadyLogin(String username);

	public JwtToken get(String accessToken) throws JsonMappingException, JsonProcessingException;

	public void save(JwtToken token) throws JsonProcessingException;

	public void remove(String accessToken);
}
