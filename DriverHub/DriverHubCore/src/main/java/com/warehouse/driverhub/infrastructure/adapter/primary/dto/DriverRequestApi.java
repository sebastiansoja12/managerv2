package com.warehouse.driverhub.infrastructure.adapter.primary.dto;

public record DriverRequestApi(Long carrierId, String firstName, String lastName, String phone, String licenseNumber) {
}
