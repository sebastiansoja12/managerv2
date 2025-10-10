package com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator;

import com.warehouse.returning.domain.vo.ReturnPackageId;
import jakarta.persistence.Embeddable;

@Embeddable
public class ReturnId {

    private Long value;

    public ReturnId() {

    }

    public ReturnId(final Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    public static ReturnId of(final ReturnPackageId returnPackageId) {
        return new ReturnId(returnPackageId.value());
    }
}
