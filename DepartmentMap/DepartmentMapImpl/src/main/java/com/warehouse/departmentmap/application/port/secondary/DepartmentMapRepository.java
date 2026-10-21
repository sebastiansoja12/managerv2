package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DepartmentMapId;
import com.warehouse.departmentmap.domain.model.DepartmentMap;

import java.util.List;
import java.util.Optional;

public interface DepartmentMapRepository {
    DepartmentMap save(final DepartmentMap departmentMap);
    Optional<DepartmentMap> findById(final DepartmentMapId departmentMapId);
    List<DepartmentMap> findByDepartment(final DepartmentId departmentId);
    void delete(final DepartmentMapId departmentMapId);
}
