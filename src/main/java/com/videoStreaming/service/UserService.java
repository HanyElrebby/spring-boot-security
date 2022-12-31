package com.videoStreaming.service;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.videoStreaming.config.AuthoritiesConstants;
import com.videoStreaming.config.Constants;
import com.videoStreaming.model.Authority;
import com.videoStreaming.model.User;
import com.videoStreaming.repository.AuthorityRepository;
import com.videoStreaming.repository.UserRepository;
import com.videoStreaming.rest.error.InvalidPasswordException;
import com.videoStreaming.rest.vm.ManagedUserVM;
import com.videoStreaming.security.SecurityUtils;
import com.videoStreaming.service.dto.UserDTO;

import net.bytebuddy.utility.RandomString;


@Service
public class UserService {
	private UserRepository userRepository;
	
	private AuthorityRepository authorityRepository;
	
	private PasswordEncoder passwordEncoder;
	

	public UserService(UserRepository userRepository, AuthorityRepository authorityRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
		this.passwordEncoder = passwordEncoder;
	}

	private boolean removeNonActivatedUser(User existingUser) {
		if (existingUser.isActivated()) {
			return false;
		}
		userRepository.delete(existingUser);
		userRepository.flush();
		return true;
	}
	
	public List<User> getAll(){
		return userRepository.findAll();
	}
	
	public User registerUser(UserDTO userDTO, String password) {
		if (!checkPasswordLength(password)) {
			throw new InvalidPasswordException();
		}
	
		userRepository.findOneByEmailIgnoreCase(userDTO.getEmail().toLowerCase()).ifPresent(existingUser -> {
			boolean removed = removeNonActivatedUser(existingUser);
			
			if (!removed) {
				throw new EmailAlreadyUsedException();
			}
		});
		
		User newUser = new User();
		newUser.setUsername(userDTO.getUsername());
		if (userDTO.getEmail() != null) {
			newUser.setEmail(userDTO.getEmail().toLowerCase());
		}
		String encryptedPassword = passwordEncoder.encode(password);
		newUser.setPassword(encryptedPassword);
		
		newUser.setActivated(false);
		
		String activationKey = RandomString.make(64);
		newUser.setActivationKey(activationKey);
		
		newUser.setLangKey(userDTO.getLangKey());
		
		Set<com.videoStreaming.model.Authority> authorities = new HashSet<Authority>();
		authorityRepository.findById(AuthoritiesConstants.ADMIN).ifPresent(authorities::add);
		newUser.setAuthorities(authorities);
		
		userRepository.save(newUser);
		
		return newUser;
	}
	
	
	
	
	public User createUser(UserDTO userDTO) {
		User user = new User();
		user.setUsername(userDTO.getUsername());
		if (userDTO.getEmail() != null) {
			user.setEmail(userDTO.getEmail().toLowerCase());
		}
		user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomString.make(60));
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomString.make(64));
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        
        return user;
	}
	
	
	
	public void updateUser(String userName, String email, String langKey, String imageUrl) {
		SecurityUtils.getCurrentUserUsername()
		.flatMap(userRepository::findOneByEmailIgnoreCase)
		.ifPresent(user -> {
			user.setUsername(userName);
			user.setEmail(email);
			user.setLangKey(langKey);
			user.setImageUrl(imageUrl);
			userRepository.save(user);
		});
	}
	
	
	public Optional<UserDTO> updateUser(UserDTO userDTO)  {
	    Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
	    
		if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
			throw new EmailAlreadyUsedException();
		}
		
		return Optional.of(userRepository
				.findById(userDTO.getId()))
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(user -> {
					user.setUsername(userDTO.getUsername());
					if (userDTO.getEmail() != null) {
						user.setEmail(userDTO.getEmail());
					}
					user.setImageUrl(userDTO.getImageUrl());
		            user.setActivated(userDTO.isActivated());
		            user.setLangKey(userDTO.getLangKey());
		            Set<Authority> managedAuthorities = user.getAuthorities();
		            managedAuthorities.clear();
		            userDTO.getAuthorities().stream()
		                   .map(authorityRepository::findById)
		                   .filter(Optional::isPresent)
		                   .map(Optional::get)
		                   .forEach(managedAuthorities::add);
		            userRepository.save(user);
		            return user;
				})
				.map(UserDTO::new);
		}
	
	
	
	   public void deleteUser(Long id) {
	       userRepository.deleteById(id);
	   }
	   
	   
	
	   
	   public User requestPasswordReset(String email) {
		   return userRepository.findOneByEmailIgnoreCase(email)
				   .map(user -> {
					   user.setResetKey(RandomString.make(64));
					   user.setResetDate(Instant.now());
					   userRepository.save(user);
					   
					//  mailService.sendPasswordResetMail(user.get());
					   
					   return user;
				   }).orElseThrow(() -> new UserNotFoundException("User with email " + email + "can not be found"));
	   }
	
	   
	   public User completePasswordReset(String newPassword, String key) {
		   return userRepository.findOneByResetKey(key)
				   .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
				   .map(user -> {
					   if (!checkPasswordLength(newPassword)) {
				            throw new InvalidPasswordException();
				       }
					   System.out.println(newPassword);
					   String encodedPassword = passwordEncoder.encode(newPassword);
					   user.setPassword(encodedPassword);
					   System.out.println(encodedPassword);
					   user.setResetKey(null);
					   user.setResetDate(null);
					   userRepository.save(user);
					   return user;
				   }).orElseThrow(() -> new UserNotFoundException("No user was found for this reset key"));
	   }
	   
	   
	   
	   public void changePassword(String currentClearTextPassword, String newPassword) {
		   SecurityUtils.getCurrentUserUsername()
		   .flatMap(userRepository::findOneByUsername)
		   .ifPresent(user -> {
			   String currentEncryptedPassword = passwordEncoder.encode(user.getPassword());
			   if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) { 
				   throw new InvalidPasswordException();
			   }
			   if (!checkPasswordLength(newPassword)) {
				   throw new InvalidPasswordException();
			   }
			   String encryptedPassword = passwordEncoder.encode(newPassword);
			   user.setPassword(encryptedPassword);
			   userRepository.save(user);
		   });
	   }
	   
	   private static boolean checkPasswordLength(String password) {
	        return !StringUtils.isEmpty(password) &&
	            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
	            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
	   }
	   
	   
	   public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
		   return userRepository.findAll(pageable).map(UserDTO::new);
	   }
	   
	   public Optional<User> getUserWithAuthorities() {
		   return SecurityUtils.getCurrentUserUsername().flatMap(userRepository::findOneWithAuthoritiesByEmailIgnoreCase);
	   }
	   
	   public UserDTO getUserWithAuthorities(Long id) {
		   return userRepository.findOneWithAuthoritiesById(id)
				   .map(UserDTO::new)
				   .get();
	   }
}
