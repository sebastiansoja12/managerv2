package com.warehouse.logisticsdelivery.api;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.dto.AssignmentOrchestrationDto;
import com.warehouse.logisticsdelivery.api.dto.DepartmentAssignmentRunDto;

import java.util.List;

public interface DepartmentAssignmentApiService {

    AssignmentOrchestrationDto orchestrate(final OperatorId operatorId);

    int recover(final OperatorId operatorId);

    int cleanup(final OperatorId operatorId);

    List<DepartmentAssignmentRunDto> findLatestRuns(final OperatorId operatorId, final int limit);
}
