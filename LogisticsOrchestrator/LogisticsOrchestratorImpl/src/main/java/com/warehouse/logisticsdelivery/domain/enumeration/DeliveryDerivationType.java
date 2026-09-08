package com.warehouse.logisticsdelivery.domain.enumeration;

public enum DeliveryDerivationType {
    RETURN,
    REDIRECT,
    SPLIT;

    public boolean reversesRoute() {
        return this == RETURN;
    }
}
