package com.ljy.chat.security;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.ljy.chat.user.User;
import com.ljy.chat.user.UserService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class SecurityTest {

	@Autowired
	private UserService userService;

	@Test
	public void save() {
		User user = new User();
		user.setUsername("nameTest");
		user.setPassword("test");
		userService.save(user);
		userService.loadUserByUsername(user.getUsername());
	}
}
