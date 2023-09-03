package com.warehouse.paypal.domain.enumeration;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    PAYPAL("PAYPAL");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }
}
