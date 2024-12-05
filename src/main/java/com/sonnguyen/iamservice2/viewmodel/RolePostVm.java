package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record RolePostVm(
        @NotBlank String name,
        @NotBlank String description,
        @NotEmpty List<Long> permissions
) {
    public Role toEntity() {
        return Role.builder()
                .name(name)
                .description(description)
                .build();
    }
}
