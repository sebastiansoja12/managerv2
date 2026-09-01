package com.warehouse.platformadministration.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateOperatorUserRequest(@NotBlank String firstName,
                                        @NotBlank String lastName,
                                        @NotBlank String username,
                                        @NotBlank String password,
                                        @NotBlank @Email String email,
                                        @NotBlank @Pattern(regexp = "USER|SUPPLIER|MANAGER|ADMIN|SUPERADMIN") String role,
                                        @NotBlank String departmentCode,
                                        @NotBlank String language) {
}
