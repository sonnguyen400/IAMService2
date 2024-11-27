package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.User;

public record UserRegistrationPostVm(
        String username,
        String password,
        String email,
        String firstname,
        String lastname
) {
    public User toEntity() {
        return User.builder()
                .username(username)
                .password(password)
                .email(email)
                .firstname(firstname)
                .lastname(lastname)
                .build();
    }
}
