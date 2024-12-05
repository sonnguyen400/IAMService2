package com.sonnguyen.iamservice2.controller;

import com.sonnguyen.iamservice2.model.RolePermission;
import com.sonnguyen.iamservice2.service.PermissionService;
import com.sonnguyen.iamservice2.service.RolePermissionService;
import com.sonnguyen.iamservice2.viewmodel.PermissionGetVm;
import com.sonnguyen.iamservice2.viewmodel.PermissionPostVm;
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
@RequestMapping("/api/v1/permission")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@SecurityRequirement(name = "bearer")
public class PermissionController {
    PermissionService permissionService;
    RolePermissionService rolePermissionService;

    @GetMapping("/ids")
    @PreAuthorize("hasPermission('ROLE_PERMISSION','READ')")
    public List<PermissionGetVm> findAllByIdIn(@RequestParam List<Long> id) {
        return permissionService.findAllByIdIn(id);
    }

    @GetMapping("/roleid/{roleId}")
    @PreAuthorize("hasPermission('ROLE_PERMISSION','READ')")
    public List<PermissionGetVm> findAllByRoleId(
            @PathVariable Long roleId) {
        return permissionService.findAllByRoleId(roleId);
    }

    @GetMapping
    @PreAuthorize("hasPermission('ROLE','READ')")
    public Page<PermissionGetVm> findAll(
            @RequestParam(name = "page", defaultValue = "0", required = false) Integer page,
            @RequestParam(name = "size", defaultValue = "10", required = false) Integer size
    ) {
        return permissionService.findAll(PageRequest.of(page, size));
    }

    @PostMapping
    @PreAuthorize("hasPermission('ROLE','CREATE')")
    public List<PermissionGetVm> createPermissions(@RequestBody @NotEmpty List<@Valid PermissionPostVm> permissionPostVms) {
        return permissionService.createPermissions(permissionPostVms);
    }

    @PostMapping("/{id}/update")
    @PreAuthorize("hasPermission('ROLE','UPDATE')")
    public PermissionGetVm updatePermissionById(
            @PathVariable Long id,
            @RequestBody @Valid PermissionPostVm permissionPostVm) {
        return permissionService.updatePermissionById(id, permissionPostVm);
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasPermission('ROLE','DELETE')")
    public void deletePermissionById(@PathVariable Long id) {
        permissionService.deleteById(id);
    }

    @GetMapping("/account/{account_id}")
    @PreAuthorize("hasPermission('ROLE','READ')")
    public List<RolePermission> findAllByAccountId(@PathVariable Long account_id) {
        return rolePermissionService.findAllByAccountId(account_id);
    }

}
