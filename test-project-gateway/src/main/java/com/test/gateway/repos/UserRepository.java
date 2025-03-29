package com.test.gateway.repos;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.test.gateway.entities.User;

import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long>{
	
	public Mono<User> findByName(String name);
}
