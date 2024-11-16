package com.warehouse.department.infrastructure.adapter.secondary.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepotEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.WARN)
public interface DepotMapper {


    DepotEntity map(Department department);

    Department map(DepotEntity depot);

    List<Department> map(List<DepotEntity> depots);

    List<DepotEntity> mapToDepotEntityList(List<Department> departments);

}
