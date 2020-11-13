package com.ljy.chat.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.chat.security.jwt.LoginSuccessHandler;
import com.ljy.chat.security.jwt.util.JwtToken;
import com.ljy.chat.security.jwt.util.ResponseError;
import com.ljy.chat.user.User;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;
	private final ObjectMapper objMapper;
	private final LoginSuccessHandler loginSuccessHandler;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
		throws AuthenticationException {
		try {
			User user = objMapper.readValue(request.getInputStream(), User.class);

			if (user.getPassword() == null || user.getUsername() == null) {
				ResponseError.error(response, 400, "아이디 혹은 비밀번호를 입력해주세요.");
				return null;
			}

			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(user.getUsername(),
				user.getPassword());
			Authentication authentication = authenticationManager.authenticate(token);
			return authentication;
		} catch (Exception e) {
			ResponseError.error(response, 400, "아이디 혹은 비밀번호를 입력해주세요.");
			return null;
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) throws IOException, ServletException {
		JwtToken token = loginSuccessHandler.successLogin(request, response, chain, authResult);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(objMapper.writeValueAsString(token));
	}
}
