package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.NotNull;

public record LoginPostVm(@NotNull String username, @NotNull String password) {
}
