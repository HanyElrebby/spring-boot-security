package com.videoStreaming.rest;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.videoStreaming.rest.vm.LoginVM;
import com.videoStreaming.security.UserDetailsImpl;
import com.videoStreaming.security.jwt.JWTFilter;
import com.videoStreaming.security.jwt.JwtTokenUtil;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:4200")
public class UserJWTController {
	@Autowired
	private AuthenticationManagerBuilder managerBuilder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@PostMapping("/authenticate")
	public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
		UsernamePasswordAuthenticationToken authenticationToken = 
				new UsernamePasswordAuthenticationToken(loginVM.getEmail(), loginVM.getPassword());
		
		Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
	    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
	    
		//boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
		String jwt = jwtTokenUtil.generateToken(userDetails, false);

		HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
	    return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
	}
	
	static class JWTToken {
		private String idToken;
		
        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
	}
}
