package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record User_Register(
	@NotBlank String username,
	@NotBlank @Email String email,
	@NotBlank String password) {
	
	public User_Register(User user) {
		this(user.getUsername(), user.getEmail(), user.getPassword());
	}
}