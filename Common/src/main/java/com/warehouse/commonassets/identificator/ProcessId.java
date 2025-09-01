package com.warehouse.commonassets.identificator;

import java.util.UUID;

public record ProcessId(UUID value) {

    public ProcessId(final UUID value) {
        this.value = value;
    }

    public UUID getValue() {
        return value;
    }
}
