package com.example.webflux;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ProfileRepository extends ReactiveCrudRepository<Profile, Long> {
}
