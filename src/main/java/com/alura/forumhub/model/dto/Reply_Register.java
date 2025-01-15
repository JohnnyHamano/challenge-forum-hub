package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Reply;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Reply_Register(
		@NotBlank String message) {
	
	public Reply_Register(Reply reply) {
		this(reply.getMessage());
	}
}