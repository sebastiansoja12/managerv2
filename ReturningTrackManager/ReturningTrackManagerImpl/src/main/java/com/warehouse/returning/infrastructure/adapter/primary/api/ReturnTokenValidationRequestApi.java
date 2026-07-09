package com.warehouse.returning.infrastructure.adapter.primary.api;

public record ReturnTokenValidationRequestApi(Long shipmentId, String returnToken) {
}
