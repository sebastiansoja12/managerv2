package com.warehouse.terminal.request;

public record DeviceHeartbeatRequestDto(String version,
                                        String manufacturer,
                                        String model,
                                        String osName,
                                        String osVersion) {
}
