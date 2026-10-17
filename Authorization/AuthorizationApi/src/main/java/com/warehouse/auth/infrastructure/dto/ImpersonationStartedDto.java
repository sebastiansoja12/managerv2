package com.warehouse.auth.infrastructure.dto;

public record ImpersonationStartedDto(Long userId, String username, Long operatorId) {
}
