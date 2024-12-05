package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.AccountRole;
import com.sonnguyen.iamservice2.model.Role;
import com.sonnguyen.iamservice2.repository.AccountRoleRepository;
import com.sonnguyen.iamservice2.repository.RoleRepository;
import com.sonnguyen.iamservice2.viewmodel.RoleGetVm;
import com.sonnguyen.iamservice2.viewmodel.RolePostVm;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleService {
    RoleRepository roleRepository;
    RolePermissionService rolePermissionService;
    AccountRoleRepository accountRoleRepository;

    public Page<RoleGetVm> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable).map(RoleGetVm::fromEntity);
    }

    public RoleGetVm createRoles(RolePostVm rolePostVms) {
        Role role = rolePostVms.toEntity();
        Role savedRole = roleRepository.save(role);
        rolePermissionService.updateRolePermission(role.getId(), rolePostVms.permissions());
        return RoleGetVm.fromEntity(savedRole);
    }

    public List<RoleGetVm> findAllByIdIn(List<Long> id) {
        return roleRepository.findAllByIdIn(id)
                .stream().map(RoleGetVm::fromEntity)
                .toList();
    }

    public RoleGetVm updateRoleById(Long roleId, RolePostVm rolePostVm) {
        Role role = rolePostVm.toEntity();
        role.setId(roleId);
        Role updatedRole = roleRepository.save(role);
        rolePermissionService.updateRolePermission(role.getId(), rolePostVm.permissions());
        return RoleGetVm.fromEntity(updatedRole);
    }

    @Transactional
    public void deleteById(Long id) {
        roleRepository.softDeleteById(id);
    }

    public List<RoleGetVm> findAllByAccountId(Long accountId) {
        List<AccountRole> accountRoles = accountRoleRepository.findAllByAccountId(accountId);
        List<Long> roleIds = accountRoles.stream().map(AccountRole::getRole_id).toList();
        return roleRepository.findAllByIdIn(roleIds)
                .stream().map(RoleGetVm::fromEntity)
                .toList();
    }
}
