package com.alura.forumhub.model;

import com.alura.forumhub.model.dto.Topic_Register;
import com.alura.forumhub.model.enumerator.Category;
import com.alura.forumhub.model.enumerator.Course;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "Topics")
@Getter
@Setter
@NoArgsConstructor
public class Topic extends Content
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title;
	
	private String message;
	
	@Enumerated(EnumType.STRING)
	private Category category;
	
	@Enumerated(EnumType.STRING)
	
	private Course course;
	
	@Column(name="author_id", nullable = false)
	private Long authorID;
	
	@Column(name="creation_date", nullable = false)
	private LocalDateTime creationDate = LocalDateTime.now();
	
	public Topic(Topic_Register topic)
	{
		this.title = topic.title();
		this.message = topic.message();
		this.category = topic.category();
		this.course = topic.course();
	}
}