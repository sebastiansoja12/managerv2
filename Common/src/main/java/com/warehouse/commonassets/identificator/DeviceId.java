package com.warehouse.commonassets.identificator;

import java.io.Serializable;
import java.util.Locale;
import java.util.Objects;

import com.warehouse.commonassets.enumeration.DeviceType;

import jakarta.persistence.Embeddable;

@Embeddable
public class DeviceId implements Serializable {
    private static final String TERMINAL_PREFIX = "TL:";
    private static final String SCANNER_PREFIX = "SC:";
    private static final String MOBILE_PREFIX = "MB:";

    private String value;

    public DeviceId(final String value) {
        this.value = value;
    }

    public DeviceId() {

    }

    public String getValue() {
        return value;
    }

    public String value() {
        return value;
    }

    public DeviceType type() {
        if (this.value == null || this.value.isBlank()) {
            throw new IllegalStateException("Device id is blank");
        }

        final String normalized = this.value.toUpperCase(Locale.ROOT);
        if (normalized.startsWith(TERMINAL_PREFIX)) {
            return DeviceType.TERMINAL;
        }
        if (normalized.startsWith(SCANNER_PREFIX)) {
            return DeviceType.SCANNER;
        }
        if (normalized.startsWith(MOBILE_PREFIX)) {
            return DeviceType.MOBILE;
        }
        throw new IllegalStateException("Unknown device type in id: " + this.value);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DeviceId deviceId)) {
            return false;
        }
        return Objects.equals(value, deviceId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }

}
