package com.alura.forumhub.controller;

import com.alura.forumhub.model.User;
import com.alura.forumhub.model.dto.User_Data;
import com.alura.forumhub.model.dto.User_Register;
import com.alura.forumhub.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;
import java.util.Objects;

@RestController
@RequestMapping("user")
public class UserController
{
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	/**
	 * @jsonProperty username  The user's username
	 * @jsonProperty email     The user's email
	 * @jsonProperty password  The user's password
	 *
	 * {
	 * 	"username": "user",
	 *  "email":    "user@email.com",
	 *  "password": "defaultpassword"
	 * }
	 **/
	@PostMapping @Transactional
	public ResponseEntity<User_Data> registerUser(@RequestBody @Valid User_Register data, UriComponentsBuilder uriBuilder) {
		User user = new User(data);
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		repository.save(user);
		
		URI uri = uriBuilder.path("/user/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).body(new User_Data(user));
	}
	
	@GetMapping
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<Page<User_Data>> listUsers(@PageableDefault(size = 10, sort = { "registrationDate" }) Pageable pagination) {
		Page<User_Data> page = repository.findAllByActiveTrue(pagination).map(User_Data::new);
		return ResponseEntity.ok(page);
	}
	
	@GetMapping("/{id}")
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<User_Data> detailUser(@PathVariable Long id) {
		User user = repository.getReferenceById(id);
		return ResponseEntity.ok(new User_Data(user));
	}
	
	@DeleteMapping("/{id}") @Transactional
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<String> deactivateUser(@PathVariable Long id) {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		User currentUser = (User) authentication.getPrincipal();
		boolean isAdmin = authentication.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
		
		User user = repository.getReferenceById(id);
		
		if (Objects.equals(currentUser.getId(), id) || isAdmin) {
			user.deactivate();
		}
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping @Transactional
	@SecurityRequirement(name = "bearer-key")
	public ResponseEntity<String> deactivateSelf() {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		User user = (User) authentication.getPrincipal();
		user.deactivate();
		
		return ResponseEntity.noContent().build();
	}
}