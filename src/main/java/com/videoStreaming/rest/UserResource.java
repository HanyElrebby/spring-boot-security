package com.videoStreaming.rest;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.videoStreaming.model.User;
import com.videoStreaming.service.UserService;
import com.videoStreaming.service.dto.UserDTO;

@RestController
@RequestMapping("/api")
public class UserResource {
	
	private UserService userService;
	
	
	public UserResource(UserService userService) {
		this.userService = userService;
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
		UserDTO user = userService.updateUser(userDTO).get();
		return new ResponseEntity<>(user, HttpStatus.OK);
	}


	@GetMapping("/users")
	public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable){
		Page<UserDTO> page = userService.getAllManagedUsers(pageable);
		
		return new ResponseEntity<>(page.getContent(), HttpStatus.OK);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<UserDTO> getUser(@PathVariable Long id){
		UserDTO user = userService.getUserWithAuthorities(id);	
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
		userService.deleteUser(id);
		return ResponseEntity.noContent().build();
	}
	
}
