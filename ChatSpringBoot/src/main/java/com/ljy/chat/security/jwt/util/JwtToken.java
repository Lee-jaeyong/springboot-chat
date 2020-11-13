package com.ljy.chat.security.jwt.util;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class JwtToken {
	@Id
	@GeneratedValue
	private Long seq;
	private String username;
	private String accessToken;
	private String refreshToken;
	private Date expire;

	@JsonIgnore
	private boolean use;

	@JsonIgnore
	public boolean isLogout() {
		return !use;
	}
}
