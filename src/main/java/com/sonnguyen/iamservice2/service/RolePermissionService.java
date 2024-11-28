package com.sonnguyen.iamservice2.service;

import com.sonnguyen.iamservice2.repository.RolePermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RolePermissionService {
    RolePermissionRepository rolePermissionRepository;
}
