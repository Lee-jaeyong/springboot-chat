package com.ljy.chat.security.jwt;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ljy.chat.security.jwt.util.JwtToken;

public interface JwtTokenReposiroty extends JpaRepository<JwtToken, Long> {
	JwtToken findFirstByUsernameAndUseOrderBySeqDesc(String username, boolean use);

	JwtToken findByAccessToken(String refreshToken);

	@Modifying
	@Transactional
	@Query("UPDATE FROM JwtToken SET use=false WHERE accessToken=:accessToken")
	void removeToken(@Param("accessToken") String accessToken);
}
