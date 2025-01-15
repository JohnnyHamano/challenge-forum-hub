package com.alura.forumhub.repository;

import com.alura.forumhub.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>
{
	@Modifying
	@Query(value = "INSERT INTO users (username, email, password) VALUES (:username, :email, :password)", nativeQuery = true)
	void register(@Param("username") String username, @Param("email") String email, @Param("password") String password);
	
	User findByUsername(String username);
	
	User findByEmail(String email);
	
	@Query(value = "SELECT * FROM users WHERE username = :usernameOrEmail OR email = :usernameOrEmail AND active = 1", nativeQuery = true)
	User findActiveUserByUsernameOrEmail(@Param("usernameOrEmail") String usernameOrEmail);
	
	Page<User> findAllByActiveTrue(Pageable pagination);
}