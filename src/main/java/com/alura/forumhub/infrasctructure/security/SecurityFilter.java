package com.alura.forumhub.infrasctructure.security;

import com.alura.forumhub.model.User;
import com.alura.forumhub.repository.UserRepository;
import com.alura.forumhub.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter
{
	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private UserRepository repository;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String jwtToken = retrieveToken(request);
		
		if (jwtToken != null) {
			String subject = tokenService.getSubject(jwtToken);
			User user = repository.findById(Long.valueOf(subject)).orElse(null);
			
			if (user != null) {
				Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			else throw new RuntimeException("User not found from token");
		}
		
		filterChain.doFilter(request, response);
	}
	
	private String retrieveToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
		return authorizationHeader != null ? authorizationHeader.replace("Bearer ", "") : null;
	}
}