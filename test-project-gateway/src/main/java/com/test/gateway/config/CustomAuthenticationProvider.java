package com.test.gateway.config;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.test.gateway.services.AuthService;

import reactor.core.publisher.Mono;

public class CustomAuthenticationProvider implements ReactiveAuthenticationManager {
	
	private final Logger LOG = Logger.getLogger("CustomAuthenticationProvider");

	@Autowired
	private AuthService authService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public Mono<Authentication> authenticate(Authentication authentication) {
		LOG.info("Principal: " + authentication.getPrincipal());
		Mono<UserDetails> userMono = authService.findByUsername(authentication.getPrincipal().toString());
		userMono.doOnError(e -> {
			throw new BadCredentialsException("Username doesn't found.");
		});
		return userMono.flatMap(userDetails -> {
			if (userDetails == null) throw new BadCredentialsException("Username doesn't found.");
			if (passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
				return Mono.just(new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword()));
			}
			throw new BadCredentialsException("Username & Password doesn't matched.");
		});
	}

}
