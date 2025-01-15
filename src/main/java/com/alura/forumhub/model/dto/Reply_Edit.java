package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Reply;
import jakarta.validation.constraints.NotBlank;

public record Reply_Edit (
	@NotBlank String message) {
	
	public Reply_Edit(Reply reply) {
		this(reply.getMessage());
	}
}
