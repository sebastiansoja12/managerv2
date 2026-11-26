package com.warehouse.logisticsdelivery.infrastructure.adapter.primary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.logisticsdelivery.api.DepartmentAssignmentApiService;
import com.warehouse.logisticsdelivery.api.dto.AssignmentOrchestrationDto;
import com.warehouse.logisticsdelivery.api.dto.DepartmentAssignmentRunDto;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPort;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.infrastructure.adapter.primary.mapper.DepartmentAssignmentRunApiMapper;

import java.util.List;

public class DepartmentAssignmentApiServiceAdapter implements DepartmentAssignmentApiService {

    private final DepartmentAssignmentPort departmentAssignmentPort;

    private final OperatorContext operatorContext;

    private final DepartmentAssignmentRunApiMapper mapper;

    public DepartmentAssignmentApiServiceAdapter(final DepartmentAssignmentPort departmentAssignmentPort,
                                                 final OperatorContext operatorContext,
                                                 final DepartmentAssignmentRunApiMapper mapper) {
        this.departmentAssignmentPort = departmentAssignmentPort;
        this.operatorContext = operatorContext;
        this.mapper = mapper;
    }

    @Override
    public AssignmentOrchestrationDto orchestrate(final OperatorId operatorId) {
        final AssignmentOrchestration orchestration = this.operatorContext.runAs(operatorId,
                this.departmentAssignmentPort::orchestrate);
        return new AssignmentOrchestrationDto(orchestration.configuredDepartments(),
                orchestration.triggeredDepartments());
    }

    @Override
    public int recover(final OperatorId operatorId) {
        return this.operatorContext.runAs(operatorId, this.departmentAssignmentPort::recover);
    }

    @Override
    public int cleanup(final OperatorId operatorId) {
        return this.operatorContext.runAs(operatorId, this.departmentAssignmentPort::cleanup);
    }

    @Override
    public List<DepartmentAssignmentRunDto> findLatestRuns(final OperatorId operatorId, final int limit) {
        return this.operatorContext.runAs(operatorId, () -> this.departmentAssignmentPort.findLatestRuns(limit))
                .stream()
                .map(this.mapper::toDto)
                .toList();
    }
}
