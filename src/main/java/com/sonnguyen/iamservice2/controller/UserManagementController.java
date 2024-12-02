package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AccountRoleService;
import com.sonnguyen.iamservice2.service.AccountService;
import com.sonnguyen.iamservice2.service.AccountServiceImpl;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
public class UserManagementController {
    AccountService accountService;
    AccountServiceImpl accountServiceImpl;
    AccountRoleService accountRoleService;
    @PostMapping
    public void createNewUser(@RequestBody UserCreationPostVm userCreationPostVm){
        accountService.create(userCreationPostVm);
    }
    @PostMapping(value = "/lock")
    public void updateAccountLockStatus(@RequestParam Boolean lock,@RequestParam String email){
        accountService.updateLockedStatusByEmail(lock,email);
    }
    @PostMapping(value = "/delete")
    public ResponseEntity<?> deleteUserByEmail(@RequestParam String email){
        return accountService.deleteByEmail(email);
    }
    @GetMapping
    @PreAuthorize("hasPermission('USER','USER_VIEW')")
    public Page<UserDetailGetVm> findAll(
            @RequestParam(name = "page",required = false,defaultValue = "0") Integer page,
            @RequestParam(name = "size",required = false,defaultValue = "10") Integer size
    ){
        return accountServiceImpl.findAll(PageRequest.of(page,size));
    }
    @PostMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return accountServiceImpl.deleteById(id);
    }
    @PostMapping("/{id}/updateRole")
    public void updateRole(@PathVariable Long id, @RequestBody List<Long> roleIds){
        accountRoleService.updateAccountRoles(id,roleIds);
    }
}
