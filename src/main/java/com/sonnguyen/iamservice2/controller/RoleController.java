package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.RolePermissionService;
import com.sonnguyen.iamservice2.service.RoleService;
import com.sonnguyen.iamservice2.viewmodel.RoleGetVm;
import com.sonnguyen.iamservice2.viewmodel.RolePostVm;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true,level= AccessLevel.PRIVATE)
public class RoleController {
    RoleService roleService;
    RolePermissionService rolePermissionService;
    @GetMapping
    public Page<RoleGetVm> findAll(
            @RequestParam(name = "page",defaultValue = "0",required = false) Integer page,
            @RequestParam(name="size", defaultValue = "10",required = false) Integer size
    ){
        return roleService.findAll(PageRequest.of(page,size));
    }
    @GetMapping("/account/{account_id}")
    public List<RoleGetVm> findAllByAccountId(@PathVariable Long account_id){
        return roleService.findAllByAccountId(account_id);
    }

    @GetMapping("/ids")
    public List<RoleGetVm> findAllByIds(
            @RequestParam List<Long> id
    ){
        return roleService.findAllByIdIn(id);
    }
    @PostMapping
    public List<RoleGetVm> createRoles(@RequestBody@NotEmpty List<@Valid RolePostVm> roles){
        return roleService.createRoles(roles);
    }

    @PostMapping(value = "/{id}/update")
    public RoleGetVm updateRoleById(@PathVariable Long id,
                                    @RequestBody RolePostVm rolePostVm){
        return roleService.updateRoleById(id,rolePostVm);
    }
    @PostMapping(value = "/{id}/delete")
    public void deleteRoleById(@PathVariable Long id){
        roleService.deleteById(id);
    }
    @PostMapping("/{id}/updatePermissions")
    public void updateRolePermission(
            @PathVariable Long id,
            @RequestBody List<Long> permissionIds){
        rolePermissionService.updateRolePermission(id,permissionIds);
    }
}
