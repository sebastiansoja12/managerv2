package com.warehouse.terminal.domain.vo;

import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DevicePairEntity;

public record DevicePairId(Long value) {
    public static DevicePairId from(final DevicePairEntity devicePairEntity) {
        return new DevicePairId(devicePairEntity.getDevicePairId());
    }
}
