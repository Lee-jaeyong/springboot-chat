package com.ljy.chat.security.jwt.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ljy.chat.security.PrinciparDetail;
import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.LoginSuccessHandler;
import com.ljy.chat.security.jwt.util.HttpParser;
import com.ljy.chat.security.jwt.util.JwtToken;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class JwtController {

	private final ObjectMapper objMapper;
	private final HttpParser httpParser;
	private final LoginSuccessHandler loginSuccessHandler;
	private final JwtStore jwtStore;

	@PostMapping("remove-token")
	public ResponseEntity<Void> logout(HttpServletRequest request) throws IOException, InvalidInputException {
		String access_token = httpParser.getAccessToken(request);
		JwtToken token = jwtStore.get(access_token);
		if (token == null) {
			return ResponseEntity.badRequest().build();
		}
		jwtStore.remove(access_token);
		return ResponseEntity.ok().build();
	}

	@PostMapping("refresh-token")
	public ResponseEntity<Void> refreshToken(Authentication authentication, HttpServletRequest request,
		HttpServletResponse response) throws IOException {
		PrinciparDetail user = (PrinciparDetail) authentication.getPrincipal();

		String refreshToken = httpParser.getRefreshToken(request);
		String accessToken = httpParser.getAccessToken(request);

		JwtToken token = jwtStore.get(accessToken);

		if (token != null && refreshToken != null && token.getRefreshToken().equals(refreshToken)) {
			jwtStore.remove(accessToken);
			JwtToken jwtToken = loginSuccessHandler.getToken(user, request, response);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(objMapper.writeValueAsString(jwtToken));
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.badRequest().build();
	}
}
