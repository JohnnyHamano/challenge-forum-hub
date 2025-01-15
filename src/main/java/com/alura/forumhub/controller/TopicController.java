package com.alura.forumhub.controller;

import com.alura.forumhub.model.Reply;
import com.alura.forumhub.model.Topic;
import com.alura.forumhub.model.User;
import com.alura.forumhub.model.dto.*;
import com.alura.forumhub.repository.ReplyRepository;
import com.alura.forumhub.repository.TopicRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping("topic")
@SecurityRequirement(name = "bearer-key")
public class TopicController
{
	@Autowired private TopicRepository topicRepository;
	@Autowired private ReplyRepository replyRepository;
	
	/** CREATE TOPIC
	 * {
	 *  "title": "The topic's title",
	 *  "message": "The topic's message text",
	 *  "category": "CATEGORY",
	 *  "course": "COURSE"
	 * }
	 **/
	@PostMapping @Transactional
	public ResponseEntity<Topic_Data> createTopic(@RequestBody @Valid Topic_Register dto, UriComponentsBuilder uriBuilder) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Topic topic = new Topic(dto);
		topic.setAuthorID(currentUser.getId());
		topicRepository.save(topic);
		
		URI uri = uriBuilder.path("/topic/{topicID}").buildAndExpand(topic.getId()).toUri();
		return ResponseEntity.created(uri).body(new Topic_Data(topic));
	}
	
	/** CREATE REPLY
	 * {
	 *  "message": "Reply text to topic"
	 * }
	 **/
	@PostMapping("/{topicID}/reply") @Transactional
	public ResponseEntity<Reply_Data> createTopicReply(@RequestBody @Valid Reply_Register dto, @PathVariable Long topicID, UriComponentsBuilder uriBuilder) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Reply reply = new Reply(dto);
		reply.setTopicID(topicID);
		reply.setAuthorID(currentUser.getId());
		replyRepository.save(reply);
		
		URI uri = uriBuilder.path("/topic/{topicID}/reply/{replyID}").buildAndExpand(reply.getId()).toUri();
		return ResponseEntity.created(uri).body(new Reply_Data(reply));
	}
	
	/** LIST ALL TOPICS
	 **/
	@GetMapping
	public ResponseEntity<Page<Topic_Data>> listTopics(@PageableDefault(size = 10, sort = { "creationDate" }) Pageable pagination) {
		Page<Topic_Data> page = topicRepository.findAllByActiveTrue(pagination).map(Topic_Data::new);
		return ResponseEntity.ok(page);
	}
	
	/** DETAIL SPECIFIC TOPIC
	 **/
	@GetMapping("/{topicID}")
	public ResponseEntity<Topic_Data> detailTopic(@PathVariable Long topicID) {
		Topic topic = topicRepository.getReferenceById(topicID);
		return ResponseEntity.ok(new Topic_Data(topic));
	}
	
	/** LIST ALL TOPIC REPLIES
	 **/
	@GetMapping("/{topicID}/replies")
	public ResponseEntity<Page<Reply_Data>> listTopicReplies(@PathVariable Long topicID, @PageableDefault(size = 10, sort = { "replyDate" }) Pageable pagination) {
		Page<Reply_Data> page = replyRepository.findAllByTopicIDAndActiveTrue(topicID, pagination).map(Reply_Data::new);
		return ResponseEntity.ok(page);
	}
	
	/** DETAIL SPECIFIC REPLY
	 **/
	@GetMapping("/{topicID}/reply/{replyID}")
	public ResponseEntity<Reply_Data> detailTopicReply(@PathVariable Long topicID, @PathVariable Long replyID) {
		Reply reply = replyRepository.getReferenceById(replyID);
		return ResponseEntity.ok(new Reply_Data(reply));
	}
	
	/** EDIT TOPIC
	 *  All properties are optional
	 * {
	 * 	"title": "new topic title",
	 * 	"message": "new topic message",
	 * 	"category": "CATEGORY",
	 * 	"course": "COURSE"
	 * }
	 **/
	@PutMapping("/{topicID}") @Transactional
	public ResponseEntity<Topic_Data> editTopic(@RequestBody Topic_Edit edit, @PathVariable Long topicID) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Topic topic = topicRepository.getReferenceById(topicID);
		
		if (Objects.equals(currentUser.getId(), topic.getAuthorID())) {
			if (edit.title() != null) topic.setTitle(edit.title());
			if (edit.message() != null) topic.setMessage(edit.message());
			if (edit.category() != null) topic.setCategory(edit.category());
			if (edit.course() != null) topic.setCourse(edit.course());
			
			topic.setEdit();
			topicRepository.save(topic);
			
			return ResponseEntity.ok(new Topic_Data(topic));
		}
		
		throw new RuntimeException("The current user is not allowed to edit this reply");
	}
	
	/** DEATIVATE TOPIC
	 **/
	@DeleteMapping("/{topicID}") @Transactional
	public ResponseEntity<String> deactivateTopic(@PathVariable Long topicID) {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		Topic topic = topicRepository.getReferenceById(topicID);
		
		if (Objects.equals(currentUser.getId(), topic.getAuthorID()) || isAdmin) {
			topic.deactivate();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	/** EDIT TOPIC REPLY
	 * {
	 * 	"message": "new reply message"
	 * }
	 **/
	@PutMapping("/{topicID}/reply/{replyID}") @Transactional
	public ResponseEntity<Reply_Data> editTopicReply(@RequestBody Reply_Edit edit, @PathVariable Long topicID, @PathVariable Long replyID) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Reply reply = replyRepository.getReferenceById(replyID);
		
		if (Objects.equals(currentUser.getId(), reply.getAuthorID())) {
			reply.setMessage(edit.message());
			reply.setEdit();
			replyRepository.save(reply);
			
			return ResponseEntity.ok(new Reply_Data(reply));
		}
		
		throw new RuntimeException("The current user is not allowed to edit this reply");
	}
	
	/** DEATIVATE TOPIC REPLY
	 **/
	@DeleteMapping("/{topicID}/reply/{replyID}") @Transactional
	public ResponseEntity<String> deactivateTopicReply(@PathVariable Long topicID, @PathVariable Long replyID) {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		Reply reply = replyRepository.getReferenceById(replyID);
		
		if ((Objects.equals(reply.getTopicID(), topicID) && Objects.equals(currentUser.getId(), reply.getAuthorID())) || isAdmin) {
			reply.deactivate();
		}
		
		return ResponseEntity.noContent().build();
	}
}