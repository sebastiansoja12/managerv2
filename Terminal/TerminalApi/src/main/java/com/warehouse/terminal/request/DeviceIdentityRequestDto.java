package com.warehouse.terminal.request;

public record DeviceIdentityRequestDto(
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
