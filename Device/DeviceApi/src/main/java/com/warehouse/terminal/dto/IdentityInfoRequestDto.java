package com.warehouse.terminal.dto;

public record IdentityInfoRequestDto(
        String assetTag,
        String barcode,
        String externalSystemId,
        String hardwareUuid,
        String imei,
        String macAddress,
        String mdmDeviceId,
        String serialNumber
) {
}

