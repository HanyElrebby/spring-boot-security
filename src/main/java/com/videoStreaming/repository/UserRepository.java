package com.videoStreaming.repository;

import org.springframework.stereotype.Repository;

import com.videoStreaming.model.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findOneByEmailIgnoreCase(String email);

	Optional<User> findByEmail(String email);

	Optional<User> findOneByUsername(String username);
	
	Optional<User> findOneByActivationKey(String activationKey);
	
	Optional<User> findOneByResetKey(String resetKey);
	
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesById(Long id);
	
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);
	
	@EntityGraph(attributePaths = "authorities")
	Optional<User> findOneWithAuthoritiesByUsername(String username);	
}
