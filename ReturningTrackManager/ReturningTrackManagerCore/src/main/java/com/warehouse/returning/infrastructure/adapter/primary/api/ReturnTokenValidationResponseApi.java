package com.warehouse.returning.infrastructure.adapter.primary.api;

public record ReturnTokenValidationResponseApi(Long shipmentId, boolean valid, String message) {
}
