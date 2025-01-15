package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Topic;
import com.alura.forumhub.model.enumerator.Category;
import com.alura.forumhub.model.enumerator.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Topic_Register(
	@NotBlank String title,
	@NotBlank String message,
	@NotNull Category category,
	@NotNull Course course) {
	
	public Topic_Register(Topic topic) {
		this(topic.getTitle(), topic.getMessage(), topic.getCategory(), topic.getCourse());
	}
}