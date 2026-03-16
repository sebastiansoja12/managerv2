package com.warehouse.returning.domain.vo;

import com.warehouse.returning.infrastructure.adapter.primary.api.dto.DepartmentCodeApi;

public record DepartmentCode(String value) {

    public static DepartmentCode of(final DepartmentCodeApi codeApi) {
        return new DepartmentCode(codeApi.value());
    }
}
