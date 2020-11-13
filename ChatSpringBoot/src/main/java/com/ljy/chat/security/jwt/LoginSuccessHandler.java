package com.ljy.chat.security.jwt;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ljy.chat.security.jwt.util.JwtToken;

public interface LoginSuccessHandler {
	JwtToken successLogin(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws JsonProcessingException;

	JwtToken getToken(UserDetails userDetails, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException;
}
