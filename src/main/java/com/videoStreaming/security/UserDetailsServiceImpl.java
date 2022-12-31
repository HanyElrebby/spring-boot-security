package com.videoStreaming.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.videoStreaming.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	private UserRepository userRepository;
	
	public UserDetailsServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(email)
				.map(user -> {
					if (!user.isActivated()) {
	                	 throw new UserNotActivatedException("User with email: " + email + " is not activated");
					}
					return new UserDetailsImpl(user);
				})
                .orElseThrow(() -> new UsernameNotFoundException("User with email: " + email + " was not found in the database"));
	}
}
