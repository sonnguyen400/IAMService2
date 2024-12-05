package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.constant.Scope;
import com.sonnguyen.iamservice2.model.Permission;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PermissionPostVm(
        @NotBlank String resource_name,
        @NotBlank String resource_code,
        @NotNull Scope scope
) {
    public Permission toEntity() {
        return Permission
                .builder()
                .resource_code(resource_code)
                .resource_name(resource_name)
                .scope(scope)
                .build();
    }
}
