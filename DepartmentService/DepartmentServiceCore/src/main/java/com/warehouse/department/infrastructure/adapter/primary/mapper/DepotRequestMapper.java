package com.warehouse.department.infrastructure.adapter.primary.mapper;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.vo.UpdateStreetRequest;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepotCodeDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.DepartmentDto;
import com.warehouse.department.infrastructure.adapter.primary.api.dto.UpdateStreetRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper
public interface DepotRequestMapper {
    
    @Mapping(target = "depotCode", source = "depotCode.value")
    Department map(DepartmentDto depot);
    
    List<Department> map(List<DepartmentDto> depots);

    DepartmentCode map(DepotCodeDto code);

    @Mapping(target = "departmentCode.value", source = "depotCode")
    UpdateStreetRequest map(UpdateStreetRequestDto updateStreetRequest);
}