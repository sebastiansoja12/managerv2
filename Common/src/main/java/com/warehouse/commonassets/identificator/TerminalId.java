package com.warehouse.commonassets.identificator;

import java.io.Serializable;

public class TerminalId implements Serializable {
    private final Long value;

    public TerminalId(final Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }
}
