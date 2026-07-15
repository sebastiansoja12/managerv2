package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.domain.vo.PickupPointDepartment;

import java.util.List;
import java.util.Optional;

public interface DepartmentDirectoryServicePort {

    Optional<PickupPointDepartment> findById(final DepartmentId departmentId);

    List<PickupPointDepartment> getCurrentOperatorDepartments();
}
