package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;

public record UserRegistrationPostVm(
        String password,
        String email,
        String firstname,
        String lastname,
        String phone,
        String address
) {
    public Account toEntity() {
        return Account.builder()
                .password(password)
                .email(email)
                .address(address)
                .lastName(lastname)
                .firstName(firstname)
                .phone(phone)
                .build();
    }
}
