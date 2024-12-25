package com.warehouse.terminal.infrastructure.adapter.secondary.mapper;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DepartmentEntity;

@Mapper
public interface DepartmentToModelMapper {
    default Department map(final DepartmentEntity department) {
        return new Department(new DepartmentCode(department.getDepartmentCode()), true);
    }
}
