package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import com.warehouse.department.domain.enumeration.DepartmentType;
import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.Address;
import com.warehouse.department.domain.vo.Coordinates;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.TaxId;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentAddress;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentCoordinates;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;

public abstract class DepartmentToModelMapper {

    public static Department map(final DepartmentEntity department) {
        if (department == null) {
            return null;
        } else {
            return new Department(new DepartmentCode(department.getDepartmentCode().getValue()),
					map(department.getDepartmentAddress()), new TaxId(department.getTaxId().value()),
					department.getTelephoneNumber(), department.getOpeningHours(), department.getEmail(),
					DepartmentType.valueOf(department.getDepartmentType().name()),
					Department.Status.valueOf(department.getStatus().name()),
                    map(department.getDepartmentCoordinates()), department.getCreatedAt(),
					department.getUpdatedAt(), department.getAdminUserId(), department.getCreatedBy(),
					department.getLastModifiedBy());
        }
    }

    public static Address map(final DepartmentAddress address) {
        return new Address(address.city(), address.street(), address.postalCode(), address.countryCode());
    }

    public static Coordinates map(final DepartmentCoordinates coordinates) {
        return new Coordinates(coordinates.latitude(), coordinates.longitude());
    }
}
