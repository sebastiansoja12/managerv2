package com.warehouse.terminal.request;

public record DeviceNetworkRequestDto(
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
