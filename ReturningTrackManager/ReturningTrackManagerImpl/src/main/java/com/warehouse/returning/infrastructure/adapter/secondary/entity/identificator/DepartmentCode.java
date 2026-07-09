package com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator;

import jakarta.persistence.Embeddable;

@Embeddable
public class DepartmentCode {
    private String value;

    public DepartmentCode() {

    }

    public DepartmentCode(final String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
