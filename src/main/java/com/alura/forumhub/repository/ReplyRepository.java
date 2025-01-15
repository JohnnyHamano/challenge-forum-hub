package com.alura.forumhub.repository;

import com.alura.forumhub.model.Reply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<Reply, Long>
{
	Page<Reply> findAllByTopicIDAndActiveTrue(Long topicID, Pageable pagination);
}