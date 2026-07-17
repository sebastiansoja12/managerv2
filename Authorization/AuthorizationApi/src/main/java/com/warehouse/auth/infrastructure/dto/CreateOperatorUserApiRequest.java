package com.warehouse.auth.infrastructure.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateOperatorUserApiRequest(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @NotBlank String username,
        @NotBlank String password,
        @NotBlank @Email String email,
        @NotBlank @Pattern(regexp = "USER|MANAGER|ADMIN|SUPPLIER") String role,
        @NotBlank String departmentCode,
        @NotBlank String language) {
}
