package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;

import java.util.Optional;

public interface DepartmentServicePort {
    Optional<PalletDepartment> findDepartment(final DepartmentId departmentId);
}
