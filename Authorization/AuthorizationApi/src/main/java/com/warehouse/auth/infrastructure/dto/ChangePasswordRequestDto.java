package com.warehouse.auth.infrastructure.dto;

public record ChangePasswordRequestDto(String currentPassword, String newPassword) {
}
