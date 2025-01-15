package com.alura.forumhub.controller;

import com.alura.forumhub.model.User;
import com.alura.forumhub.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController
{
	@Autowired private AuthenticationManager manager;
	@Autowired private TokenService tokenService;
	
	/**
	 * @jsonProperty login     Can be username or email
	 * @jsonProperty password  The user's password
	 *
	 * {
	 *  "login": "JohnnyHamano",
	 *  "password": "091124Max"
	 * }
	 */
	@PostMapping @Transactional
	public ResponseEntity<JWT_Token> login(@RequestBody @Valid AuthenticationData data) {
		Authentication authToken = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		
		try {
			Authentication authentication = manager.authenticate(authToken);
			String jwtToken = tokenService.generateToken((User) authentication.getPrincipal());
			
			return ResponseEntity.ok(new JWT_Token(jwtToken));
		}
		catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public record AuthenticationData(String login, String password) { }
	public record JWT_Token(String token) { }
}