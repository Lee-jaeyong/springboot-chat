package com.ljy.chat.security.jwt.impl;

import java.time.Duration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.util.JwtToken;

//@Service
@PropertySource("classpath:application-redis.properties")
public class JwtRedisService implements JwtStore {

	@Autowired
	private RedisTemplate<String, String> template;

	private ValueOperations<String, String> valueOper;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${ACCESS_KEY}")
	private String ACCESS_KEY;

	@Value("${REFRESH_KEY}")
	private String REFRESH_KEY;

	@PostConstruct
	public void init() {
		valueOper = template.opsForValue();
	}

	public JwtToken getRefreshToken(String accessToken) throws JsonMappingException, JsonProcessingException {
		String token = valueOper.get(REFRESH_KEY + accessToken);
		return objectMapper.readValue(token, JwtToken.class);
	}

	public JwtToken get(String accessToken) throws JsonMappingException, JsonProcessingException {
		if (accessToken == null) {
			return null;
		}

		String token = valueOper.get(ACCESS_KEY + accessToken);

		if (token == null) {
			return null;
		}

		return objectMapper.readValue(token, JwtToken.class);
	}

	public void save(JwtToken token) throws JsonProcessingException {
		valueOper.set(REFRESH_KEY + token.getAccessToken(), objectMapper.writeValueAsString(token), Duration.ofDays(24));
		valueOper.set(ACCESS_KEY + token.getAccessToken(), objectMapper.writeValueAsString(token), Duration.ofMinutes(180));
	}

	public void remove(String accessToken) {
		template.delete(ACCESS_KEY + accessToken);
		template.delete(REFRESH_KEY + accessToken);
	}

	@Override
	public JwtToken alreadyLogin(String username) {
		return null;
	}
}
