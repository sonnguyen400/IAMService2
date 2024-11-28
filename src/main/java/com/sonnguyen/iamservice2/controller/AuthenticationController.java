package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AuthenticationService;
import com.sonnguyen.iamservice2.service.AccountService;
import com.sonnguyen.iamservice2.service.AuthenticationServiceImpl;
import com.sonnguyen.iamservice2.viewmodel.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuthenticationController {
    AuthenticationService authenticationService;
    AccountService accountService;
    AuthenticationServiceImpl authenticationServiceImpl;
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginPostVm loginPostVm) {
        return authenticationService.login(loginPostVm);
    }
    @GetMapping(value = "/login")
    public ResponseEntity<?> oauth2LoginToken(@RequestBody(required = false) LoginPostVm loginPostVm) {
        return authenticationService.login(loginPostVm);
    }
    @PostMapping(value = "/login/accept")
    public ResponseTokenVm oauth2LoginToken(@RequestBody(required = false) AcceptedLoginRequestVm acceptedLoginRequestVm) {
        return authenticationServiceImpl.acceptLoginRequest(acceptedLoginRequestVm);
    }
    @PostMapping(value = "/register")
    public String register(@RequestBody UserRegistrationPostVm registrationPostVm){
        accountService.createUser(registrationPostVm);
        return "registered successfully";
    }
    @GetMapping(value = "/verify")
    public ResponseEntity<?> requestVerifyAccount(@RequestParam String email){
        return authenticationServiceImpl.requestVerifyAccount(email);
    }
    @GetMapping(value = "/verify/{code}")
    public ResponseEntity<?> requestVerifyCode(@PathVariable String code){
        return authenticationServiceImpl.acceptVerifyAccount(code);
    }
    @PostMapping(value = "/logout")
    public void logout(@RequestBody RequestTokenVm requestTokenVm){
        authenticationService.logout(requestTokenVm);
    }
    @PostMapping(value = "/token/refresh")
    public ResponseTokenVm refreshToken(@RequestBody RefreshTokenPostVm refreshTokenPostVm){
        return authenticationService.refreshToken(refreshTokenPostVm.refresh_token());
    }

}
