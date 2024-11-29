package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.NotNull;

public record LoginResponseViewModel(@NotNull String access_token) {
}
