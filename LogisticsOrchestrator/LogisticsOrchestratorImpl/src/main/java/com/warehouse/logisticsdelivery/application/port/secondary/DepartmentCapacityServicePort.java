package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.domain.vo.DepartmentCapacity;

import java.util.List;

public interface DepartmentCapacityServicePort {

    List<DepartmentCapacity> findDepartments();
}
