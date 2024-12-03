package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.AccountRoleService;
import com.sonnguyen.iamservice2.service.AccountService;
import com.sonnguyen.iamservice2.service.AccountServiceImpl;
import com.sonnguyen.iamservice2.specification.AccountSpecification;
import com.sonnguyen.iamservice2.specification.DynamicSearch;
import com.sonnguyen.iamservice2.utils.SearchUtils;
import com.sonnguyen.iamservice2.viewmodel.UserCreationPostVm;
import com.sonnguyen.iamservice2.viewmodel.UserDetailGetVm;
import com.sonnguyen.iamservice2.viewmodel.UserProfilePostVm;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
@Slf4j
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
            @RequestParam(name = "size",required = false,defaultValue = "10") Integer size,
            HttpServletRequest request
    ){
        Sort sort=Sort.by(SearchUtils.parseSort(request.getParameterMap()));
        List<AccountSpecification> accountSpecification=parseRequestToSpecification(request);
        return accountServiceImpl.findAll(accountSpecification,PageRequest.of(page,size).withSort(sort));
    }
    @PostMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteById(@PathVariable Long id){
        return accountServiceImpl.deleteById(id);
    }
    @PostMapping("/{id}/updateRole")
    public void updateRole(@PathVariable Long id, @RequestBody List<Long> roleIds){
        accountRoleService.updateAccountRoles(id,roleIds);
    }
    @PostMapping("/{id}/resetpassword")
    public void resetPassword(@PathVariable Long id,
                              @RequestParam String password){
        accountService.resetPasswordByAccountId(id,password);
    }
    @PostMapping("/{id}/updateprofile")
    public void updateAccountProfile(@PathVariable Long id,
                                     @RequestBody UserProfilePostVm userProfilePostVm){
        accountServiceImpl.updateAccountProfileById(id,userProfilePostVm);
    }

    private List<AccountSpecification> parseRequestToSpecification(HttpServletRequest request){
        List<AccountSpecification> accountSpecifications=new ArrayList<>();
        Map<String,String[]> parameterMap=request.getParameterMap();
        List<DynamicSearch> list= SearchUtils.parseOperator(parameterMap);
        list.forEach(dynamicSearch -> {
            accountSpecifications.add(new AccountSpecification(dynamicSearch));
        });
        return accountSpecifications;
    }
}
