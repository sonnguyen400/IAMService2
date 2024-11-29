package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Permission;
import jakarta.validation.constraints.NotBlank;

public record PermissionGetVm(
        @NotBlank Long id,
        @NotBlank String resource_name,
        @NotBlank String resource_code,
        @NotBlank String scope
) {
    public static PermissionGetVm fromEntity(Permission permission){
        return new PermissionGetVm(
                permission.getId(),
                permission.getResource_name(),
                permission.getResource_code(),
                permission.getScope()
        );
    }
}
