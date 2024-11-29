package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AccountService;
import com.sonnguyen.iamservice2.service.AccountServiceImpl;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserManagementController {
    @Autowired
    AccountService accountService;
    @Autowired
    AccountServiceImpl accountServiceImpl;

    @PostMapping
    public void createNewUser(@RequestBody UserCreationPostVm userCreationPostVm){
        accountService.create(userCreationPostVm);
    }
    @PostMapping(value = "/lock")
    public void updateAccountLockStatus(@RequestParam Boolean lock,@RequestParam String email){
        accountService.updateLockedStatusByEmail(lock,email);
    }
    @GetMapping
    public Page<UserDetailGetVm> findAll(
            @RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
            @RequestParam(name = "size",required = false,defaultValue = "10") Integer size
    ){
        return accountServiceImpl.findAll(PageRequest.of(page,size));
    }
}
