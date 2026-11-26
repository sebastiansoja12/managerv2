package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentConfig;

import java.util.List;

public interface DepartmentAssignmentConfigServicePort {

    List<DepartmentAssignmentConfig> findConfigs();
}
