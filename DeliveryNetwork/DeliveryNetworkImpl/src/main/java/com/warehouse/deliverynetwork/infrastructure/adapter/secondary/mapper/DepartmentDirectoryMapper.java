package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.department.api.dto.DepartmentDirectoryEntryDto;

public class DepartmentDirectoryMapper {

    public DepartmentNode toModel(final DepartmentDirectoryEntryDto department) {
        return new DepartmentNode(
                new DepartmentId(department.departmentId().value()),
                new DepartmentCode(department.departmentCode().value()),
                DepartmentType.valueOf(department.departmentType().name()),
                DepartmentStatus.valueOf(department.status().name()));
    }
}
