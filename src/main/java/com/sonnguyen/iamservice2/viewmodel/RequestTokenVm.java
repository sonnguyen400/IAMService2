package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.NotNull;

public record RequestTokenVm(@NotNull String access_token, @NotNull String refresh_token) {
}
