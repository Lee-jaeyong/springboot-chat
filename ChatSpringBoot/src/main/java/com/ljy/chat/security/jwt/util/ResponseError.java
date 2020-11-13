package com.ljy.chat.security.jwt.util;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;

public class ResponseError {

	public static void error(HttpServletResponse response, int status, String message) {
		try {
			response.setStatus(status);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write(message);
		} catch (IOException e) {
		}
	}
}
