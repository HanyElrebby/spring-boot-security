package com.videoStreaming.security;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.videoStreaming.config.AuthoritiesConstants;


public final class SecurityUtils {
	
	public static Optional<String> getCurrentUserUsername() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		return Optional.ofNullable(extractPrinciple(securityContext.getAuthentication()));
	}
	
	private static String extractPrinciple(Authentication authentication) {
		if (authentication == null) {
			return null;
		} else if (authentication.getPrincipal() instanceof UserDetails) {
			UserDetailsImpl user = (UserDetailsImpl) authentication.getPrincipal();
			return user.getEmail();
		} else if (authentication.getPrincipal() instanceof String) {
			return (String) authentication.getPrincipal();
		}

		return null;
	}
	
	public boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null &&
				getAuthorities(authentication).noneMatch(AuthoritiesConstants.ANONYMOUS::equals);
	}
	
	public static boolean isCurrentUserInRole(String authority) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		return authentication != null &&
				getAuthorities(authentication).anyMatch(authority::equals);
	}
	
	private static Stream<String> getAuthorities(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.map(authority -> authority.getAuthority());
	}
}
