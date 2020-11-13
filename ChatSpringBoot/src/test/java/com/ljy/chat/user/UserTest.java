package com.ljy.chat.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {

	@Autowired
	private MockMvc mvc;

	private ObjectMapper objMapper = new ObjectMapper();

	@Test
	public void saveAndloginTest() throws Exception {
		UserDTO user = new UserDTO();
		user.setUsername("dlwodyd202");
		user.setPassword("dlwodyd");

		mvc.perform(post("/api/user").contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(user)))
			.andExpect(status().isOk());

		String token = mvc
			.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(user)))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		mvc.perform(post("/api/remove-token").header("Authorization", token)).andExpect(status().isOk());
		mvc.perform(post("/api/remove-token").header("Authorization", token)).andExpect(status().isForbidden());

		token = mvc
			.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(user)))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Thread.sleep(500);

		String reLogin = mvc
			.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON).content(objMapper.writeValueAsString(user)))
			.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		assertEquals(reLogin, token);

		Thread.sleep(500);

		String refresh = mvc.perform(post("/api/refresh-token").header("Authorization", token)).andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();

		mvc.perform(post("/api/remove-token").header("Authorization", token)).andExpect(status().isForbidden());
		mvc.perform(post("/api/remove-token").header("Authorization", refresh)).andExpect(status().isOk());
	}
}
