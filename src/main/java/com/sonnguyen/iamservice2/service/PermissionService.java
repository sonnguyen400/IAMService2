package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.exception.InvalidArgumentException;
import com.sonnguyen.iamservice2.model.Permission;
import com.sonnguyen.iamservice2.model.Role;
import com.sonnguyen.iamservice2.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;
    RoleService roleService;
    public void saveAll(List<Permission> list){
        permissionRepository.saveAll(list);
    }
}
