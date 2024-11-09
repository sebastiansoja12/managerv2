package com.warehouse.commonassets.identificator;

import java.io.Serializable;

public class TerminalId implements Serializable {
    private final Double value;

    public TerminalId(final Double value) {
        this.value = value;
    }

    public Double getValue() {
        return value;
    }
}
