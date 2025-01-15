package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Topic;
import com.alura.forumhub.model.enumerator.Category;
import com.alura.forumhub.model.enumerator.Course;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public record Topic_Data(
	String title,
	String message,
	Category category,
	Course course,
	@JsonAlias("author_id") Long authorID,
	@JsonAlias("creation_date") LocalDateTime creationDate) {
		
	public Topic_Data(Topic topic) {
		this(topic.getTitle(), topic.getMessage(), topic.getCategory(), topic.getCourse(), topic.getAuthorID(), topic.getCreationDate());
	}
}
