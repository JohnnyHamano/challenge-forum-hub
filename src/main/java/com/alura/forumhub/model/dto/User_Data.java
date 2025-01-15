package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.User;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public record User_Data(
	String username,
	String email,
	@JsonAlias("registration_date") LocalDateTime registrationDate) {
	
	public User_Data(User user) {
		this(user.getUsername(), user.getEmail(), user.getRegistrationDate());
	}
}