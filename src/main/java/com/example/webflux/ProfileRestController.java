package com.example.webflux;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
class ProfileRestController {

    private final MediaType mediaType = MediaType.APPLICATION_JSON_UTF8;
    private final ProfileService profileRepository;

    ProfileRestController(ProfileService profileRepository) {
        this.profileRepository = profileRepository;
    }


    @GetMapping
    Publisher<Profile> getAll() {
        return this.profileRepository.all();
    }

    @GetMapping("/{id}")
    Publisher<Profile> getById(@PathVariable("id") Long id) {
        return this.profileRepository.get(id);
    }

    @PostMapping
    Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile) {
        return this.profileRepository
                .create(profile.getEmail())
                .map(p -> ResponseEntity.created(URI.create("/profiles/" + p.getId()))
                        .contentType(mediaType)
                        .build());
    }
}
