package com.sonnguyen.iamservice2.viewmodel;

import com.sonnguyen.iamservice2.model.Role;
import jakarta.validation.constraints.NotBlank;

public record RolePostVm(
        @NotBlank String name,
        @NotBlank String description
) {
    public Role toEntity(){
        return Role.builder()
                .name(name)
                .description(description)
                .build();
    }
}
