package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;

import java.util.Date;

public record UserDetailGetVm(
        Long id,
        String email,
        String firstname,
        String lastname,
        String phone,
        String address,
        String picture,
        String dateOfBirth,
        Boolean locked,
        Boolean verified
) {
    public static UserDetailGetVm fromEntity(Account account) {
        Date dateOfBirth = account.getDateOfBirth();
        String dob = dateOfBirth != null ? dateOfBirth.toString() : "";
        return new UserDetailGetVm(account.getId(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.getPhone(),
                account.getAddress(),
                account.getPicture(),
                dob,
                account.isLocked(),
                account.isVerified());
    }
}
