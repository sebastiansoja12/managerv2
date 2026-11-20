package com.warehouse.analytics.application.port.primary;

import com.warehouse.analytics.application.port.primary.command.FindDepartmentCostsCommand;
import com.warehouse.analytics.application.port.primary.command.RecordDepartmentCostCommand;
import com.warehouse.analytics.domain.model.DepartmentCost;
import com.warehouse.commonassets.identificator.DepartmentCostId;

import java.util.List;

public interface DepartmentCostPort {

    DepartmentCostId record(final RecordDepartmentCostCommand command);

    List<DepartmentCost> find(final FindDepartmentCostsCommand command);

    void delete(final DepartmentCostId costId);
}
