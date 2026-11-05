package com.warehouse.process.infrastructure.adapter.primary.api;

public record DeviceInformationResponseApi(
        String deviceId,
        String departmentCode,
        Long userId,
        String username,
        String deviceType,
        String deviceUserType,
        String version) {
}
