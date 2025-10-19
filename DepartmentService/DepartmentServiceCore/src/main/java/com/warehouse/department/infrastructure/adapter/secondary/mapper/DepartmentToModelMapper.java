package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;

public abstract class DepartmentToModelMapper {

    public static Department map(final DepartmentEntity department) {
        if (department == null) {
            return null;
        } else {
            return new Department(new DepartmentCode(department.getDepartmentCode().getValue()), department.getCity(),
                    department.getStreet(), department.getCountry(), department.getPostalCode(), department.getNip(),
                    department.getTelephoneNumber(), department.getOpeningHours(), department.getEmail(), department.isActive(),
                    department.getCountryCode(), DepartmentType.valueOf(department.getDepartmentType().name()),
                    department.getCreatedAt(), department.getUpdatedAt());
        }
    }
}
