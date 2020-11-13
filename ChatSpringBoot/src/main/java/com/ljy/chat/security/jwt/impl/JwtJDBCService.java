package com.ljy.chat.security.jwt.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ljy.chat.security.jwt.JwtStore;
import com.ljy.chat.security.jwt.JwtTokenReposiroty;
import com.ljy.chat.security.jwt.util.JwtToken;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JwtJDBCService implements JwtStore {
	private final JwtTokenReposiroty jwtTokenReposiroty;

	@Override
	@Transactional(readOnly = true)
	public JwtToken alreadyLogin(String username) {
		return jwtTokenReposiroty.findFirstByUsernameAndUseOrderBySeqDesc(username, true);
	}

	@Override
	@Transactional(readOnly = true)
	public JwtToken get(String accessToken) {
		if (accessToken == null) {
			return null;
		}
		return jwtTokenReposiroty.findByAccessToken(accessToken);
	}

	@Override
	public void save(JwtToken token) {
		jwtTokenReposiroty.save(token);
	}

	@Override
	public void remove(String accessToken) {
		jwtTokenReposiroty.removeToken(accessToken);
	}
}
