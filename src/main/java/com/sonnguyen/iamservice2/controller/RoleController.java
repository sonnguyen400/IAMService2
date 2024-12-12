package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.service.RolePermissionService;
import com.sonnguyen.iamservice2.service.RoleService;
import com.sonnguyen.iamservice2.viewmodel.RoleGetVm;
import com.sonnguyen.iamservice2.viewmodel.RolePostVm;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/role")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "bearer")
public class RoleController {
    RoleService roleService;
    RolePermissionService rolePermissionService;

    @GetMapping
    @PreAuthorize("hasPermission('ROLE','READ')")
    public Page<RoleGetVm> findAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ) {
        return roleService.findAll(PageRequest.of(page, size));
    }

    @GetMapping("/account/{account_id}")
    @PreAuthorize("hasPermission('ROLE','READ')")
    public List<RoleGetVm> findAllByAccountId(@PathVariable Long account_id) {
        return roleService.findAllByAccountId(account_id);
    }

    @GetMapping("/ids")
    @PreAuthorize("hasPermission('ROLE','READ')")
    public List<RoleGetVm> findAllByIds(
            @RequestParam List<Long> id
    ) {
        return roleService.findAllByIdIn(id);
    }

    @PostMapping
    @PreAuthorize("hasPermission('ROLE','CREATE')")
    public RoleGetVm createRoles(@RequestBody @Valid RolePostVm roles) {
        return roleService.createRoles(roles);
    }

    @PostMapping(value = "/{id}/update")
    @PreAuthorize("hasPermission('ROLE','UPDATE')")
    public RoleGetVm updateRoleById(@PathVariable Long id,
                                    @RequestBody RolePostVm rolePostVm) {
        return roleService.updateRoleById(id, rolePostVm);
    }

    @PostMapping(value = "/{id}/delete")
    @PreAuthorize("hasPermission('ROLE','DELETE')")
    public void deleteRoleById(@PathVariable Long id) {
        roleService.deleteById(id);
    }
}
