package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Account;

public record UserDetailGetVm(
        String email,
        String firstname,
        String lastname,
        String phone,
        String address,
        String picture,
        Boolean locked,
        Boolean verified
) {
    public static UserDetailGetVm fromEntity(Account account){
        return new UserDetailGetVm(account.getEmail(),
                account.getFirstName(),
                account.getLastName(),
                account.getPhone(),
                account.getAddress(),
                account.getPicture(),
                account.getLocked(),
                account.getVerified());
    }
}
