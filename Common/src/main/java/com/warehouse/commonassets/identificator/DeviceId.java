package com.warehouse.commonassets.identificator;

import com.warehouse.commonassets.enumeration.DeviceType;
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

    public DeviceType type() {
        final long v = this.value();

        final long prefix = v / 1_000_000_000_000_000L;

        return switch ((int) prefix) {
            case 101 -> DeviceType.TERMINAL;
            case 202 -> DeviceType.SCANNER;
            case 303 -> DeviceType.MOBILE;
            default -> throw new IllegalStateException("Unknown device type in id: " + v);
        };
    }

}
