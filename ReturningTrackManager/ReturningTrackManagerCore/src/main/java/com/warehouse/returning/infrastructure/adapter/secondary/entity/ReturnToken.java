package com.warehouse.returning.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Embeddable;

@Embeddable
public class ReturnToken {
    private String value;
    public ReturnToken() {

    }
    public ReturnToken(final String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
