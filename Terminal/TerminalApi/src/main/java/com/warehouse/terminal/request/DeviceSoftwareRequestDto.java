package com.warehouse.terminal.request;

import java.util.Set;

public record DeviceSoftwareRequestDto(
        String osName,
        String osVersion,
        String firmwareVersion,
        String kernelVersion,
        String bootloaderVersion,
        String appVersion,
        String buildNumber,
        Boolean rooted,
        Boolean developerModeEnabled,
        Set<String> installedApps
) {
}
