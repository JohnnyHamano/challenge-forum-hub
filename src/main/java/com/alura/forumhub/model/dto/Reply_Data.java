package com.alura.forumhub.model.dto;

import com.alura.forumhub.model.Reply;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;

public record Reply_Data(
		@JsonAlias("topic_id") Long topicID,
		@JsonAlias("author_id") Long authorID,
		String message,
		@JsonAlias("reply_date") LocalDateTime replyDate) {
	
	public Reply_Data(Reply reply) {
		this(reply.getTopicID(), reply.getAuthorID(), reply.getMessage(), reply.getReplyDate());
	}
}