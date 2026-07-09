package com.warehouse.deliveryreturn.infrastructure.adapter.secondary.api.dto;

public record ReturnTokenValidationResponseDto(Long shipmentId, boolean valid, String message) {
}
