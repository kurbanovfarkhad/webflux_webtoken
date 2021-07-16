package com.example.webflux;

import org.springframework.data.annotation.Id;

public class Profile {
    @Id
    private Long id;
    private String email;

    public Profile() {
    }

    public Profile(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public Profile setId(Long id) {
        this.id = id;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public Profile setEmail(String email) {
        this.email = email;
        return this;
    }
}
