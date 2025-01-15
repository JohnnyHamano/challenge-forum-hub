package com.alura.forumhub.model;

import com.alura.forumhub.model.dto.Reply_Register;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Replies")
@Getter
@Setter
@NoArgsConstructor
public class Reply extends Content
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name="topic_id", nullable = false)
	private Long topicID;
	
	@Column(name="author_id", nullable = false)
	private Long authorID;
	
	private String message;
	
	@Column(name="reply_date", nullable = false)
	private LocalDateTime replyDate = LocalDateTime.now();
	
	public Reply(Reply_Register reply)
	{
		this.message = reply.message();
	}
}