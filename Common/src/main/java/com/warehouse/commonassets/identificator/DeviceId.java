package com.warehouse.commonassets.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public class DeviceId {
    private Long value;

    public DeviceId(final Long value) {
        this.value = value;
    }

    public DeviceId() {

    }

    public Long getValue() {
        return value;
    }

    public Long value() {
        return value;
    }
}
