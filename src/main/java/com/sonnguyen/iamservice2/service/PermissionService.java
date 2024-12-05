package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.model.Permission;
import com.sonnguyen.iamservice2.model.RolePermission;
import com.sonnguyen.iamservice2.repository.PermissionRepository;
import com.sonnguyen.iamservice2.repository.RolePermissionRepository;
import com.sonnguyen.iamservice2.viewmodel.PermissionGetVm;
import com.sonnguyen.iamservice2.viewmodel.PermissionPostVm;
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
public class PermissionService {
    PermissionRepository permissionRepository;
    RolePermissionRepository rolePermissionRepository;

    public Page<PermissionGetVm> findAll(Pageable pageable) {
        return permissionRepository.findAll(pageable).map(PermissionGetVm::fromEntity);
    }

    public List<PermissionGetVm> createPermissions(List<PermissionPostVm> permissionPostVm) {
        List<Permission> permissions = permissionPostVm.stream().map(PermissionPostVm::toEntity).toList();
        return permissionRepository
                .saveAll(permissions)
                .stream().map(PermissionGetVm::fromEntity)
                .toList();
    }

    public List<PermissionGetVm> findAllByIdIn(List<Long> id) {
        return permissionRepository.findAllByIdIn(id)
                .stream().map(PermissionGetVm::fromEntity)
                .toList();
    }

    public PermissionGetVm updatePermissionById(Long permissionId, PermissionPostVm permissionPostVm) {
        Permission permission = permissionPostVm.toEntity();
        permission.setId(permissionId);
        Permission updatedPermission = permissionRepository.save(permission);
        return PermissionGetVm.fromEntity(updatedPermission);
    }

    @Transactional
    public void deleteById(Long id) {
        permissionRepository.softDeleteById(id);
    }

    public List<PermissionGetVm> findAllByRoleId(Long roleId) {
        List<RolePermission> rolePermissions = rolePermissionRepository.findAllByRoleId(roleId);
        List<Long> permissionIds = rolePermissions.stream().map(RolePermission::getPermission_id).toList();
        return findAllByIdIn(permissionIds);
    }
}
