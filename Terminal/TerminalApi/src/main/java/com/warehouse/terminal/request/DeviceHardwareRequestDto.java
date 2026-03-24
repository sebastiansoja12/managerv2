package com.warehouse.terminal.request;

public record DeviceHardwareRequestDto(
        String manufacturer,
        String model,
        String productName,
        String cpu,
        Integer ramMb,
        Integer storageMb,
        String screenResolution,
        Boolean hasScanner,
        Boolean hasCamera,
        Boolean hasNfc,
        Boolean hasGps,
        Boolean ruggedized
) {
}
