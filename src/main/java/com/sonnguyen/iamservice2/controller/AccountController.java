package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.model.Account;
import com.sonnguyen.iamservice2.service.AccountServiceImpl;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
@SecurityRequirement(name = "bearer")
public class AccountController {
    AccountServiceImpl accountService;
    @GetMapping
    public UserDetailGetVm findCurrentUser(Authentication authentication) {
        Account account= accountService.findByEmail(authentication.getPrincipal().toString()).get();
        return UserDetailGetVm.fromEntity(account);
    }
}
