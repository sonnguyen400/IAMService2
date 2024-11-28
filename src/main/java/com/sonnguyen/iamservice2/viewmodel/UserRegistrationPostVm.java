package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;

public record UserRegistrationPostVm(
        String password,
        String email
) {
    public Account toEntity() {
        return Account.builder()
                .password(password)
                .email(email)
                .build();
    }
}
