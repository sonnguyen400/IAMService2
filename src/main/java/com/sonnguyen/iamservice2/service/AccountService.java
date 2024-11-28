package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;

public interface AccountService {
    void register(UserRegistrationPostVm userRegistrationPostVm);
    void create(UserCreationPostVm userRegistrationPostVm);

}
