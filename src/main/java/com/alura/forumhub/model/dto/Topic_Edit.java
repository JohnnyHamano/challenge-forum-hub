package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Topic;
import com.alura.forumhub.model.enumerator.Category;
import com.alura.forumhub.model.enumerator.Course;
import jakarta.validation.constraints.NotBlank;

public record Topic_Edit(
	@NotBlank String title,
	@NotBlank String message,
	@NotBlank Category category,
	@NotBlank Course course) {
	
	public Topic_Edit(Topic topic) {
		this(topic.getTitle(), topic.getMessage(), topic.getCategory(), topic.getCourse());
	}
}