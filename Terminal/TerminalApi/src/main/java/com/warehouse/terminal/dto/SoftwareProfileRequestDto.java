package com.warehouse.terminal.dto;

public record SoftwareProfileRequestDto(
        String osName,
        String osVersion,
        String firmwareVersion,
        String kernelVersion,
        String bootloaderVersion,
        String appVersion,
        String buildNumber,
        Boolean rooted,
        Boolean developerModeEnabled
) {
}
