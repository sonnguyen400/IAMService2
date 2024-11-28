package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AccountService;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserManagementController {
    @Autowired
    AccountService accountService;

    @PostMapping
    public void createNewUser(@RequestBody UserCreationPostVm userCreationPostVm){
        accountService.create(userCreationPostVm);
    }
}
