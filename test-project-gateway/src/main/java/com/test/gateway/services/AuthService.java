package com.test.gateway.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.test.gateway.entities.CustomUserDetails;
import com.test.gateway.entities.User;
import com.test.gateway.repos.UserRepository;

import reactor.core.publisher.Mono;

@Service
public class AuthService implements ReactiveUserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
		
	public Mono<User> signupUser(User user) {
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userRepository.save(user);
	}
	
	@Override
	public Mono<UserDetails> findByUsername(String username) {
		return userRepository.findByName(username).flatMap(u -> {
			if (u == null) throw new UsernameNotFoundException("User not found.");
			return Mono.just(new CustomUserDetails(u.getName(), u.getEmail(), u.getPassword(), List.of()));
		});
	}
}
