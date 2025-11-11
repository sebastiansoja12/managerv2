package com.warehouse.department.infrastructure.adapter.primary.mapper;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCreateResponse;
import com.warehouse.department.domain.vo.IdentificationNumberChangeResponse;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.*;

import java.util.HashMap;
import java.util.Map;

public abstract class ResponseMapper {

    public static DepartmentCreateApiResponse mapToCreateApiResponse(final DepartmentCreateResponse response) {
        final Map<DepartmentApi, Boolean> departments = new HashMap<>();
        for (final Map.Entry<Department, Boolean> entry : response.departmentMap().entrySet()) {
            departments.put(map(entry.getKey()), entry.getValue());
        }
        return new DepartmentCreateApiResponse(departments);
    }

    public static DepartmentApi map(final Department department) {
        final DepartmentCodeApi departmentCode = new DepartmentCodeApi(department.getDepartmentCode().getValue());
		return new DepartmentApi(departmentCode, map(department.getAddress()), department.getNip(),
				department.getTelephoneNumber(), department.getOpeningHours(), department.getEmail(), department.getActive(),
                department.getDepartmentType().name(), department.getStatus().name(), department.getCreatedAt(), department.getUpdatedAt());
    }

    public static IdentificationNumberChangeApiResponse map(final IdentificationNumberChangeResponse response) {
        return new IdentificationNumberChangeApiResponse(new DepartmentCodeApi(response.departmentCode().getValue()),
                response.oldIdentificationNumber(), response.newIdentificationNumber());
    }

    private static AddressApi map(final Address address) {
        return new AddressApi(address.city(), address.street(), address.postalCode(), address.countryCode().name());
    }
}
