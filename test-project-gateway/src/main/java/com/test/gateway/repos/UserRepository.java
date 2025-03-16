package com.test.gateway.repos;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

import com.test.gateway.entities.User;

@Repository
public interface UserRepository extends R2dbcRepository<User, Long>{

}
