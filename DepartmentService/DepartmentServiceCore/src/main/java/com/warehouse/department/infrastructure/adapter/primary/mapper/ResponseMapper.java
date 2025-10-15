package com.warehouse.department.infrastructure.adapter.primary.mapper;

import java.util.HashMap;
import java.util.Map;

import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCodeApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentCreateApiResponse;

public abstract class ResponseMapper {

    public static DepartmentCreateApiResponse mapToCreateApiResponse(final DepartmentCreateResponse response) {
        final Map<DepartmentCodeApi, Boolean> departments = new HashMap<>();
        for (final Map.Entry<DepartmentCode, Boolean> entry : response.departmentMap().entrySet()) {
            departments.put(new DepartmentCodeApi(entry.getKey().getValue()), entry.getValue());
        }
        return new DepartmentCreateApiResponse(departments);
    }
}
