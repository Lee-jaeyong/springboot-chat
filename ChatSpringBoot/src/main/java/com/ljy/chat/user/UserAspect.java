package com.ljy.chat.user;

import java.time.Duration;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Aspect
@Component
@SuppressWarnings("unchecked")
public class UserAspect {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	private ValueOperations<String, String> valueOperations;

	private final String user = "user:";

	@Around("execution(* com.ljy.chat.user.UserRepository.findByUsername(..))")
	public Object findByUsername(ProceedingJoinPoint joinPoint) throws Throwable {
		ObjectMapper objMapper = new ObjectMapper();
		String username = (String) joinPoint.getArgs()[0];
		String redisUser = valueOperations.get(user + username);
		if (redisUser != null) {
			Optional<User> result = Optional.of(objMapper.readValue(redisUser, User.class));
			return result;
		}
		Optional<User> result = (Optional<User>) joinPoint.proceed();
		if (result.isPresent()) {
			valueOperations.set(user + username, objMapper.writeValueAsString(result.get()), Duration.ofMinutes(60));
		}
		return result;
	}

	@PostConstruct
	public void init() {
		valueOperations = redisTemplate.opsForValue();
	}

}
