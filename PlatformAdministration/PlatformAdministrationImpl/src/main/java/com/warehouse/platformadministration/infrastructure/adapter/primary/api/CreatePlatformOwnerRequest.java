package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreatePlatformOwnerRequest(@NotBlank String username,
                                         @NotBlank String password,
                                         @NotBlank String firstName,
                                         @NotBlank String lastName,
                                         @NotBlank @Email String email) {
}
