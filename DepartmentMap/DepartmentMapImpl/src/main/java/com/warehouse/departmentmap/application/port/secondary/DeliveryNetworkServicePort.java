package com.warehouse.departmentmap.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.departmentmap.domain.vo.DepartmentDirection;

import java.util.List;

public interface DeliveryNetworkServicePort {
    List<DepartmentDirection> findDirections(final DepartmentId departmentId);
}
