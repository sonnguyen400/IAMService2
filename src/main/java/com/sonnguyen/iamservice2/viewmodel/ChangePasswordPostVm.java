package com.sonnguyen.iamservice2.viewmodel;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordPostVm(
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters at length") String oldPassword,
        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters at length") String newPassword,
        @NotBlank @Email String email) {
}