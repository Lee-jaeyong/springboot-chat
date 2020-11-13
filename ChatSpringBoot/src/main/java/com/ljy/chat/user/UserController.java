package com.ljy.chat.user;

import javax.validation.Valid;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

	private final UserService userService;

	@PostMapping
	public ResponseEntity<EntityModel<UserDTO>> save(@RequestBody @Valid UserDTO user, Errors error) {
		if (error.hasErrors()) {
			return ResponseEntity.badRequest().build();
		}
		user = userService.save(user.toEntity()).toDto();
		Link link = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(this.getClass()).save(user, error)).withSelfRel();
		return ResponseEntity.ok(new EntityModel<UserDTO>(user, link));
	}
}
