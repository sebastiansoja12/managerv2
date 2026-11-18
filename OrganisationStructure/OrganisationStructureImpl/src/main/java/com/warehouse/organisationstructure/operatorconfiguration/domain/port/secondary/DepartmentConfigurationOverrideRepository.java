package com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.DepartmentConfigurationOverride;

import java.util.List;
import java.util.Optional;

public interface DepartmentConfigurationOverrideRepository {

    Optional<DepartmentConfigurationOverride> findByDepartmentId(final DepartmentId departmentId);

    List<DepartmentConfigurationOverride> findAll();

    void save(final DepartmentConfigurationOverride override);

    void delete(final DepartmentId departmentId);
}
