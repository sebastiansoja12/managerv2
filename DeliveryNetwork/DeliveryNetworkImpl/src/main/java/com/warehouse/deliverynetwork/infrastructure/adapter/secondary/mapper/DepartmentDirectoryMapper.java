package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentLocation;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.organisationstructure.department.api.dto.CoordinatesDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDirectoryEntryDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;

public class DepartmentDirectoryMapper {

    public DepartmentNode toModel(final DepartmentDirectoryEntryDto department) {
        return new DepartmentNode(
                new DepartmentId(department.departmentId().value()),
                new DepartmentCode(department.departmentCode().value()),
                DepartmentType.valueOf(department.departmentType().name()),
                DepartmentStatus.valueOf(department.status().name()),
                toModel(department.coordinates()));
    }

    public DepartmentAddress toAddress(final DepartmentDto department) {
        return new DepartmentAddress(
                department.street(),
                department.zipCode(),
                department.city(),
                department.countryCode());
    }

    private DepartmentLocation toModel(final CoordinatesDto coordinates) {
        if (coordinates == null || coordinates.latitude() == null || coordinates.longitude() == null) {
            return null;
        }
        return new DepartmentLocation(coordinates.latitude(), coordinates.longitude());
    }
}
