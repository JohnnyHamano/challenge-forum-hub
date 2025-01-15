package com.alura.forumhub.controller;

import com.alura.forumhub.model.Reply;
import com.alura.forumhub.model.User;
import com.alura.forumhub.model.dto.Reply_Data;
import com.alura.forumhub.model.dto.Reply_Edit;
import com.alura.forumhub.model.dto.Reply_Register;
import com.alura.forumhub.repository.ReplyRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("reply")
@SecurityRequirement(name = "bearer-key")
public class ReplyController
{
	@Autowired
	private ReplyRepository repository;
	
	/** CREATE REPLY
	 * {
	 *  "message": "Reply text to topic"
	 * }
	 **/
	@PostMapping("/{topicID}") @Transactional
	public ResponseEntity<Reply_Data> createReply(@RequestBody @Valid Reply_Register dto, @PathVariable Long topicID, UriComponentsBuilder uriBuilder) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Reply reply = new Reply(dto);
		reply.setTopicID(topicID);
		reply.setAuthorID(currentUser.getId());
		repository.save(reply);

		URI uri = uriBuilder.path("/reply/{id}").buildAndExpand(reply.getId()).toUri();
		return ResponseEntity.created(uri).body(new Reply_Data(reply));
	}
	
	/** LIST ALL TOPIC REPLIES
	 **/
	@GetMapping("/list/{topicID}")
	public ResponseEntity<Page<Reply_Data>> listReplies(@PathVariable Long topicID, @PageableDefault(size = 10, sort = { "replyDate" }) Pageable pagination) {
		Page<Reply_Data> page = repository.findAllByTopicIDAndActiveTrue(topicID, pagination).map(Reply_Data::new);
		return ResponseEntity.ok(page);
	}
	
	/** DETAIL SPECIFIC REPLY
	 **/
	@GetMapping("/{id}")
	public ResponseEntity<Reply_Data> detailReply(@PathVariable Long id) {
		Reply reply = repository.getReferenceById(id);
		return ResponseEntity.ok(new Reply_Data(reply));
	}
	
	/** EDIT REPLY
	 * {
	 * 	"message": "new reply message"
	 * }
	 **/
	@PutMapping("/{id}") @Transactional
	public ResponseEntity<Reply_Data> editReply(@RequestBody Reply_Edit edit, @PathVariable Long id) {
		User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Reply reply = repository.getReferenceById(id);

		if (Objects.equals(currentUser.getId(), reply.getAuthorID())) {
			reply.setMessage(edit.message());
			reply.setEdit();
			repository.save(reply);

			return ResponseEntity.ok(new Reply_Data(reply));
		}

		throw new RuntimeException("The current user is not allowed to edit this reply");
	}
	
	/** DEATIVATE TOPIC REPLY
	 **/
	@DeleteMapping("/{id}") @Transactional
	public ResponseEntity<String> deactivateReply(@PathVariable Long id) {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		Reply reply = repository.getReferenceById(id);
		
		if (Objects.equals(currentUser.getId(), reply.getAuthorID()) || isAdmin) {
			reply.deactivate();
		}

		return ResponseEntity.noContent().build();
	}
}