package com.warehouse.returning.domain.vo;

public record OperatorId(Long value) {

    public static OperatorId of(final Long value) {
        return new OperatorId(value);
    }
}
