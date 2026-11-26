package com.warehouse.platformadministration.application.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.platformadministration.application.port.secondary.DepartmentAssignmentRunServicePort;
import com.warehouse.platformadministration.application.port.secondary.OperatorServicePort;
import com.warehouse.platformadministration.application.service.OperatorJobRegistry;
import com.warehouse.platformadministration.domain.enumeration.OperatorJobType;
import com.warehouse.platformadministration.domain.exception.OperatorNotFoundException;
import com.warehouse.platformadministration.domain.vo.DepartmentAssignmentRunView;
import com.warehouse.platformadministration.domain.vo.OperatorJobRun;
import com.warehouse.platformadministration.domain.vo.OperatorProfile;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class OperatorAdminActionPortImpl implements OperatorAdminActionPort {

    static final int COURIER_ASSIGNMENT_RUNS_LIMIT = 50;

    private static final String ACTIVE_STATUS = "ACTIVE";

    private final OperatorServicePort operatorServicePort;

    private final OperatorJobRegistry operatorJobRegistry;

    private final DepartmentAssignmentRunServicePort departmentAssignmentRunServicePort;

    public OperatorAdminActionPortImpl(final OperatorServicePort operatorServicePort,
                                       final OperatorJobRegistry operatorJobRegistry,
                                       final DepartmentAssignmentRunServicePort departmentAssignmentRunServicePort) {
        this.operatorServicePort = operatorServicePort;
        this.operatorJobRegistry = operatorJobRegistry;
        this.departmentAssignmentRunServicePort = departmentAssignmentRunServicePort;
    }

    @Override
    public List<OperatorJobType> jobTypes() {
        return operatorJobRegistry.jobTypes();
    }

    @Override
    public OperatorJobRun runJob(final OperatorId operatorId, final OperatorJobType jobType) {
        final OperatorProfile operator = operatorServicePort.findProfile(operatorId)
                .orElseThrow(() -> new OperatorNotFoundException(operatorId));
        if (!ACTIVE_STATUS.equals(operator.status())) {
            throw new IllegalStateException("Jobs can be started only for an active operator");
        }
        final OperatorJobRun run = operatorJobRegistry.find(jobType)
                .orElseThrow(() -> new IllegalArgumentException("Unknown operator job " + jobType))
                .run(operatorId);
        log.info("Platform owner ran job {} for operator {}: affected={}, total={}, asynchronous={}", jobType,
                operatorId.value(), run.affected(), run.total(), run.asynchronous());
        return run;
    }

    @Override
    public List<DepartmentAssignmentRunView> findCourierAssignmentRuns(final OperatorId operatorId) {
        if (operatorServicePort.findProfile(operatorId).isEmpty()) {
            throw new OperatorNotFoundException(operatorId);
        }
        return departmentAssignmentRunServicePort.findLatest(operatorId, COURIER_ASSIGNMENT_RUNS_LIMIT);
    }
}
