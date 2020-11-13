package com.ljy.chat.security.jwt.impl;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ljy.chat.security.PrinciparDetail;
import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.LoginSuccessHandler;
import com.ljy.chat.security.jwt.util.JwtToken;
import com.ljy.chat.security.jwt.util.JwtTokenFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtLoginSuccessHandler implements LoginSuccessHandler {

	private final JwtStore jwtStore;

	@Override
	public JwtToken successLogin(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws JsonProcessingException {
		PrinciparDetail detail = (PrinciparDetail) authResult.getPrincipal();
		return getToken(detail, request, response);
	}

	@Override
	public JwtToken getToken(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response)
		throws JsonProcessingException {
		JwtToken responseToken = alreadyLogin(userDetails.getUsername());
		if (responseToken != null) {
			return responseToken;
		}
		responseToken = JwtTokenFactory.create(userDetails, Algorithm.HMAC256("cos"));
		jwtStore.save(responseToken);
		return responseToken;
	}

	private JwtToken alreadyLogin(String username) {
		return jwtStore.alreadyLogin(username);
	}

}
