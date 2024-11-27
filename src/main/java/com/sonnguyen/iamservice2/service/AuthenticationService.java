package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> login(LoginPostVm loginPostVm);
}
