package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentAddress;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;

public abstract class DepartmentToModelMapper {

    public static Department map(final DepartmentEntity department) {
        if (department == null) {
            return null;
        } else {
            return new Department(new DepartmentCode(department.getDepartmentCode().getValue()),
                    map(department.getDepartmentAddress()), department.getNip(),
                    department.getTelephoneNumber(), department.getOpeningHours(), department.getEmail(), department.isActive(),
                    DepartmentType.valueOf(department.getDepartmentType().name()), Department.Status.valueOf(department.getStatus().name()),
                    department.getCreatedAt(), department.getUpdatedAt());
        }
    }

    public static Address map(final DepartmentAddress address) {
        return new Address(address.city(), address.street(), address.postalCode(), address.countryCode());
    }
}
