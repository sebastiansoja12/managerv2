package com.warehouse.terminal.domain.vo;

import java.util.UUID;

public record DeviceActivationCodeId(UUID value) {

    public static DeviceActivationCodeId generate() {
        return new DeviceActivationCodeId(UUID.randomUUID());
    }
}
