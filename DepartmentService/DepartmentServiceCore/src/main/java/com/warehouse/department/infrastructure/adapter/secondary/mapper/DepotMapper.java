package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotMapper {


    DepartmentEntity map(Department department);

    Department map(DepartmentEntity depot);

    List<Department> map(List<DepartmentEntity> depots);

    List<DepartmentEntity> mapToDepotEntityList(List<Department> departments);

}
