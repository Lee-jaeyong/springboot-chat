package com.ljy.chat.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
public class User {
	@Id
	@GeneratedValue
	private long id;
	private String username;
	private String password;
	private String roles;
	private boolean state;

	@JsonIgnore
	public boolean isDelete() {
		return !state;
	}

	@JsonIgnore
	public List<String> getRoleList() {
		if (roles.length() > 0) {
			return Arrays.asList(roles.split(","));
		}
		return Collections.emptyList();
	}

	@JsonIgnore
	public UserDTO toDto() {
		return new ModelMapper().map(this, UserDTO.class);
	}
}
