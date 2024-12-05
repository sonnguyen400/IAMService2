package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;
import jakarta.validation.constraints.NotBlank;

public record UserCreationPostVm(@NotBlank String password,
                                 @NotBlank String email,
                                 String firstname,
                                 String lastname,
                                 String phone,
                                 String address,
                                 Boolean locked,
                                 Boolean verified
) {
    public Account toEntity() {
        return Account.builder()
                .password(password)
                .email(email)
                .address(address)
                .lastName(lastname)
                .firstName(firstname)
                .phone(phone)
                .locked(locked)
                .verified(verified)
                .build();
    }
}
