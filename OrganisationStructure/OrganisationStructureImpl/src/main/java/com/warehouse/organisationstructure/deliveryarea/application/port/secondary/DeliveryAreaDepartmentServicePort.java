package com.warehouse.organisationstructure.deliveryarea.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.vo.DeliveryAreaDepartment;

import java.util.List;
import java.util.Optional;

public interface DeliveryAreaDepartmentServicePort {

    Optional<DeliveryAreaDepartment> findDepartment(final DepartmentId departmentId);

    List<DeliveryAreaDepartment> findDepartments();
}
