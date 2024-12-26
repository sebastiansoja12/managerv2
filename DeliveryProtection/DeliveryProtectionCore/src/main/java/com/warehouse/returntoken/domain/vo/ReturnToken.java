package com.warehouse.returntoken.domain.vo;

public class ReturnToken {
    private final String value;

    public ReturnToken(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
