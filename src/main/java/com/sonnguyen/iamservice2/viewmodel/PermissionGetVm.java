package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.constant.Scope;
import com.sonnguyen.iamservice2.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PermissionGetVm(
        @NotBlank Long id,
        @NotBlank String resource_name,
        @NotBlank String resource_code,
        @NotNull Scope scope
) {
    public static PermissionGetVm fromEntity(Permission permission) {
        return new PermissionGetVm(
                permission.getId(),
                permission.getResource_name(),
                permission.getResource_code(),
                permission.getScope()
        );
    }
}
