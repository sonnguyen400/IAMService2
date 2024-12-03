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
        Date dateOfBirth,
        Boolean locked,
        Boolean verified
) {
    public static UserDetailGetVm fromEntity(Account account){
        return new UserDetailGetVm(account.getId(),
                account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.getPhone(),
                account.getAddress(),
                account.getPicture(),
                account.getDateOfBirth(),
                account.isLocked(),
                account.isVerified());
    }
}
