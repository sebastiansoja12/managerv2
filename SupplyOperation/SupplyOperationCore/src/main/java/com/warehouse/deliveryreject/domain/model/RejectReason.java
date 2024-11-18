package com.warehouse.deliveryreject.domain.model;

public class RejectReason {
    private String value;

    public RejectReason(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }
}
