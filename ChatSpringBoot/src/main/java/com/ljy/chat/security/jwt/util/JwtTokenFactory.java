package com.ljy.chat.security.jwt.util;

import java.util.Date;

import org.springframework.security.core.userdetails.UserDetails;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.ljy.chat.security.PrinciparDetail;

public class JwtTokenFactory {

	public static JwtToken create(UserDetails userDetail, Algorithm algorithm) {
		PrinciparDetail userDetails = (PrinciparDetail) userDetail;
		String username = userDetails.getUsername();

		String jwtToken = JWT.create().withSubject("jwt토큰").withExpiresAt(new Date(System.currentTimeMillis() + (1800000)))
			.withClaim("username", username).sign(algorithm);

		String refreshToken = JWT.create().withSubject("jwt토큰").withExpiresAt(new Date(System.currentTimeMillis() + (2073600000)))
			.withClaim("username", username).sign(algorithm);

		Date expire = JWT.require(Algorithm.HMAC256("cos")).build().verify(jwtToken).getExpiresAt();
		
		JwtToken responseToken = new JwtToken();
		responseToken.setUsername(username);
		responseToken.setAccessToken("Bearer " + jwtToken);
		responseToken.setExpire(expire);
		responseToken.setRefreshToken(refreshToken);
		responseToken.setUse(true);
		return responseToken;
	}
}
