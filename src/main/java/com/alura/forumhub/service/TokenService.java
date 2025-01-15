package com.alura.forumhub.service;

import com.alura.forumhub.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService
{
	@Value("${api.security.token.secret}")
	private String secret;
	
	private final String ISSUER = "ForumHub";
	
	public String generateToken(User user) {
		final int EXPIRATION_HOURS = 24;
		
		try {
			return JWT.create()
				.withIssuer(ISSUER)
				.withSubject(user.getId().toString())
				.withClaim("user", user.getUsername())
				.withClaim("email", user.getEmail())
				.withExpiresAt(LocalDateTime.now().plusHours(EXPIRATION_HOURS).toInstant(ZoneOffset.of("-03:00")))
				.sign(Algorithm.HMAC256(secret));
		}
		catch (JWTCreationException exception) {
			throw new RuntimeException("Error generating JWT token", exception);
		}
	}
	
	public String getSubject(String token) {
		try {
			DecodedJWT decodedJWT = verifyToken(token);
			return decodedJWT.getSubject();
		}
		catch (JWTVerificationException exception) {
			throw new RuntimeException("Invalid JWT token", exception);
		}
	}
	
	public String getUser(String token) {
		return getClaim(token, "user");
	}
	
	public String getEmail(String token) {
		return getClaim(token, "email");
	}
	
	String getClaim(String token, String claim) {
		try {
			DecodedJWT decodedJWT = verifyToken(token);
			return decodedJWT.getClaim(claim).toString();
		}
		catch (JWTVerificationException exception) {
			throw new RuntimeException("Invalid JWT token", exception);
		}
	}
	
	DecodedJWT verifyToken(String token) {
		return JWT.require(Algorithm.HMAC256(secret)).withIssuer(ISSUER).build().verify(token);
	}
}