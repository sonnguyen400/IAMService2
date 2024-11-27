package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AuthenticationService;
import com.sonnguyen.iamservice2.service.UserService;
import com.sonnguyen.iamservice2.viewmodel.LoginPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserRegistrationPostVm;
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
    UserService userService;
    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestBody LoginPostVm loginPostVm) {
        return authenticationService.login(loginPostVm);
    }
    @GetMapping(value = "/login")
    public ResponseEntity<?> oauth2LoginToken(@RequestBody(required = false) LoginPostVm loginPostVm) {
        return authenticationService.login(loginPostVm);
    }
    @PostMapping(value = "/register")
    public String register(@RequestBody UserRegistrationPostVm registrationPostVm){
        userService.createUser(registrationPostVm);
        return "registered successfully";
    }
}
