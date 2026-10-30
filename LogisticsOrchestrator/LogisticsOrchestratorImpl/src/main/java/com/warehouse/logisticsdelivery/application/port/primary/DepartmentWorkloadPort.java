package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentLoad;

import java.util.List;

public interface DepartmentWorkloadPort {

    long countActiveDeliveries(final DepartmentId departmentId);

    void transferActiveDeliveries(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId);

    List<DepartmentLoad> findDepartmentLoads();
}
