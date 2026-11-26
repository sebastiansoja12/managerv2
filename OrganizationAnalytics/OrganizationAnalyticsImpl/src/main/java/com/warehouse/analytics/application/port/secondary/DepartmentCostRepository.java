package com.warehouse.analytics.application.port.secondary;

import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.analytics.domain.vo.AnalyticsPeriod;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentCostId;

import java.util.List;
import java.util.Optional;

public interface DepartmentCostRepository {

    void create(final DepartmentCost departmentCost);

    Optional<DepartmentCost> findById(final DepartmentCostId costId);

    void delete(final DepartmentCostId costId);

    List<DepartmentCost> find(final AnalyticsPeriod period, final Optional<DepartmentCode> departmentCode);
}
