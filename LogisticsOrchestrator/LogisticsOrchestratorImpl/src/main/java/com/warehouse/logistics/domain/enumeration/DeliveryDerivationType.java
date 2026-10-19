package com.warehouse.logistics.domain.enumeration;

public enum DeliveryDerivationType {
    RETURN,
    REDIRECT,
    SPLIT;

    public boolean reversesRoute() {
        return this == RETURN;
    }
}
