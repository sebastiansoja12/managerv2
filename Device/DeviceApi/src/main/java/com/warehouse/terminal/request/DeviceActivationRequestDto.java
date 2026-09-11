package com.warehouse.terminal.request;

public record DeviceActivationRequestDto(String activationCode,
                                         String installationId,
                                         String deviceType,
                                         String appVersion,
                                         String manufacturer,
                                         String model,
                                         String osName,
                                         String osVersion) {
}
