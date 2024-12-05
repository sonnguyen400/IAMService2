package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.AccountRole;
import com.sonnguyen.iamservice2.model.RolePermission;
import com.sonnguyen.iamservice2.repository.AccountRoleRepository;
import com.sonnguyen.iamservice2.repository.RolePermissionRepository;
import com.sonnguyen.iamservice2.viewmodel.PermissionGetVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RolePermissionService {
    AccountRoleRepository accountRoleRepository;
    RolePermissionRepository rolePermissionRepository;
    PermissionService permissionService;

    public List<RolePermission> findAllByRoleId(Long roleId) {
        return rolePermissionRepository.findAllByRoleId(roleId);
    }

    List<RolePermission> findAllByRoleIdIn(List<Long> roleIds) {
        return rolePermissionRepository.findAllByRoleIdIn(roleIds);
    }

    @Transactional
    public void updateRolePermission(Long roleId, List<Long> permissionIds) {
        rolePermissionRepository.softDeleteByRoleId(roleId);
        List<PermissionGetVm> permissions = permissionService.findAllByIdIn(permissionIds);
        List<RolePermission> rolePermissions = permissions
                .stream()
                .map(permission_ -> createByRoleIdAndPermission(roleId, permission_))
                .toList();
        rolePermissionRepository.saveAll(rolePermissions);
    }

    public RolePermission createByRoleIdAndPermission(Long roleId, PermissionGetVm permission) {

        return RolePermission.builder()
                .role_id(roleId)
                .permission_id(permission.id())
                .scope(permission.scope())
                .resource_code(permission.resource_code())
                .build();
    }

    public List<RolePermission> findAllByAccountId(Long accountId) {
        List<AccountRole> accountRoles = accountRoleRepository.findAllByAccountId(accountId);
        List<Long> roleIds = accountRoles.stream().map(AccountRole::getRole_id).toList();
        return rolePermissionRepository.findAllByRoleIdIn(roleIds);
    }
}
