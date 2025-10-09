package com.warehouse.returning.domain.vo;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;

public record ReturnPackageId(Long value) {
    public static ReturnPackageId from(final ReturnId returnId) {
        return new ReturnPackageId(returnId.getValue());
    }
}
