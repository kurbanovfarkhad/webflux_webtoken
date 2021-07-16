package com.example.webflux;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProfileService {
    private final ApplicationEventPublisher publisher;
    private final ProfileRepository profileRepository;

    ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all() {
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(Long id) {
        return this.profileRepository.findById(id);
    }

    public Mono<Profile> create(String email) {
        return this.profileRepository
                .save(new Profile(email))
                .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}
