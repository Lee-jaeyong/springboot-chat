package com.ljy.chat.security.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ljy.chat.security.PrinciparDetail;
import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.util.HttpParser;
import com.ljy.chat.security.jwt.util.JwtToken;
import com.ljy.chat.user.UserService;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
	private final UserService userService;
	private final JwtStore jwtStore;
	private final HttpParser httpParser;

	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserService userService, JwtStore jwtStore,
		HttpParser httpParser) {
		super(authenticationManager);
		this.userService = userService;
		this.jwtStore = jwtStore;
		this.httpParser = httpParser;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		String accessToken = httpParser.getAccessToken(request);
		if (accessToken == null || !accessToken.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		try {
			String username = JWT.require(Algorithm.HMAC256("cos")).build().verify(accessToken.replace("Bearer ", ""))
				.getClaim("username").asString();

			JwtToken logoutCheckToken = jwtStore.get(accessToken);

			if (logoutCheckToken.isLogout()) {
				chain.doFilter(request, response);
				return;
			}

			PrinciparDetail detail = ((PrinciparDetail) userService.loadUserByUsername(username));

			Authentication authentication = new UsernamePasswordAuthenticationToken(detail, null, detail.getAuthorities());

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
		}
		chain.doFilter(request, response);
	}
}
