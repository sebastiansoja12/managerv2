package com.warehouse.pallet.domain.enumeration;

import java.math.BigDecimal;

public enum Unit {
    G, KG, T;

    public BigDecimal toKilograms(final BigDecimal value) {
        return switch (this) {
            case G -> value.movePointLeft(3);
            case KG -> value;
            case T -> value.movePointRight(3);
        };
    }
}
