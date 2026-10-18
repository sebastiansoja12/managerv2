package com.warehouse.driverhub.infrastructure.adapter.primary.dto;

public record CarrierRequestApi(String name, String taxId, String phone, String email, String defaultTransportType) {
}
