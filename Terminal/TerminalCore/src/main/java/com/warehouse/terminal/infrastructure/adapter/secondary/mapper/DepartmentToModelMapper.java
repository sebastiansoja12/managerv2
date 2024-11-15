package com.warehouse.terminal.infrastructure.adapter.secondary.mapper;

import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DepartmentEntity;
import org.mapstruct.Mapper;

@Mapper
public interface DepartmentToModelMapper {
    Department map(final DepartmentEntity department);
}
