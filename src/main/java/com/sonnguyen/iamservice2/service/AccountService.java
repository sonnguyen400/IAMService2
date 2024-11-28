package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;

public interface AccountService {
    void createUser(UserRegistrationPostVm userRegistrationPostVm);
    void enableAccountByEmail(String email);
}
