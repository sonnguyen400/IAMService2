package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.NotNull;

public record ResponseTokenVm(@NotNull String access_token, @NotNull String refresh_token) {
}
