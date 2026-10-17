package com.warehouse.auth.infrastructure.dto;

public record UserApiKeyDto(UserIdDto userId,
                            String username,
                            String firstName,
                            String lastName,
                            String role,
                            String departmentCode) {
}
