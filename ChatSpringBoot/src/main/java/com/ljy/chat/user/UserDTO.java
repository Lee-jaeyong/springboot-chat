package com.ljy.chat.user;

import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
	
	@NotNull
	private String username;
	
	@NotNull
	private String password;

	public User toEntity() {
		User user = new ModelMapper().map(this, User.class);
		return user;
	}
}
