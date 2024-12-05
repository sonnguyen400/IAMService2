package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.ChangePasswordPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
import org.springframework.http.ResponseEntity;

public interface AccountService {
    void register(UserRegistrationPostVm userRegistrationPostVm);

    void create(UserCreationPostVm userRegistrationPostVm);

    void updateLockedStatusByEmail(Boolean isLocked, String email);

    ResponseEntity<?> deleteByEmail(String email);

    ResponseEntity<?> deleteById(Object id);

    void resetPasswordByAccountId(Long accountId, String rawPassword);

    void updatePasswordByEmail(ChangePasswordPostVm changePasswordPostVm);
}
