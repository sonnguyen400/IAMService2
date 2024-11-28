package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.NotNull;

public record LoginPostVm(@NotNull String email, @NotNull String password) {
}
