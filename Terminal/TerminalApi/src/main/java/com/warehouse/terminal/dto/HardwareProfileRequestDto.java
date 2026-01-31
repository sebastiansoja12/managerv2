package com.warehouse.terminal.dto;

public record HardwareProfileRequestDto(
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

