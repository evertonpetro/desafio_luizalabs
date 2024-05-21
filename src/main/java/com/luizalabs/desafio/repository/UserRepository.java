package com.luizalabs.desafio.repository;

import com.luizalabs.desafio.repository.model.User;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends R2dbcRepository<User, Integer> {
    Mono<User> findById(int userId);
}
