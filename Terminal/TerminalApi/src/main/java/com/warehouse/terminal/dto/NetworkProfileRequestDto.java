package com.warehouse.terminal.dto;

public record NetworkProfileRequestDto(
        String ipAddress,
        String publicIpAddress,
        String networkType,
        String carrier,
        String simSerial,
        Boolean roaming,
        Boolean vpnConnected,
        String wifiSsid,
        String bluetoothMac
) {
}

