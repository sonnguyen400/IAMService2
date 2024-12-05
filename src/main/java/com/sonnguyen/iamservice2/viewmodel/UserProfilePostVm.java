package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record UserProfilePostVm(
        @NotBlank String lastname,
        @NotBlank String firstname,
        @NotBlank String address,
        @NotBlank String phone,
        @NotBlank Date dateOfBirth
) {
    public Account toEntity() {
        return Account
                .builder()
                .lastName(lastname)
                .firstName(firstname)
                .address(address)
                .phone(phone)
                .dateOfBirth(dateOfBirth)
                .build();
    }
}
