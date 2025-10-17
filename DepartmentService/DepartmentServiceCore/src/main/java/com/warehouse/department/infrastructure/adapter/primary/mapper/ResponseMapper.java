package com.warehouse.department.infrastructure.adapter.primary.mapper;

import java.util.HashMap;
import java.util.Map;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.AddressApi;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentApi;
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

    public static DepartmentApi map(final Department department) {
        final DepartmentCodeApi departmentCode = new DepartmentCodeApi(department.getDepartmentCode().getValue());
		return new DepartmentApi(departmentCode, map(department.getAddress()), department.getNip(),
				department.getTelephoneNumber(), department.getOpeningHours(), department.getActive(),
                department.getDepartmentType().name(), department.getCreatedAt(), department.getUpdatedAt());
    }

    private static AddressApi map(final Address address) {
        return new AddressApi(address.city(), address.street(), address.country(), address.postalCode(), address.countryCode().name());
    }
}
