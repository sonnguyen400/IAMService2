package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.ChangePasswordPostVm;
import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.RequestTokenVm;
import com.sonnguyen.iamservice2.viewmodel.ResponseTokenVm;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> login(LoginPostVm loginPostVm);

    ResponseTokenVm refreshToken(String refreshToken);

    void logout(RequestTokenVm requestTokenVm);

    ResponseEntity<?> changePassword(ChangePasswordPostVm changePasswordPostVm);
}
