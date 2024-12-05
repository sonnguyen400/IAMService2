package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Role;

public record RoleGetVm(
        Long id,
        String name,
        String description
) {
    public static RoleGetVm fromEntity(Role role) {
        return new RoleGetVm(
                role.getId(),
                role.getName(),
                role.getDescription()
        );
    }
}
