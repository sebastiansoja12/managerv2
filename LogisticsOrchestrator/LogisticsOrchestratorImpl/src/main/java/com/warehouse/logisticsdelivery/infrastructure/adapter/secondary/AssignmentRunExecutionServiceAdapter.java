package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logisticsdelivery.application.port.primary.DepartmentAssignmentPort;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunExecutionServicePort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;

public class AssignmentRunExecutionServiceAdapter implements AssignmentRunExecutionServicePort {

    private static final Logger log = LoggerFactory.getLogger(AssignmentRunExecutionServiceAdapter.class);

    private final TaskExecutor taskExecutor;

    private final OperatorContext operatorContext;

    private final OperatorContextProvider operatorContextProvider;

    private final ObjectProvider<DepartmentAssignmentPort> departmentAssignmentPort;

    public AssignmentRunExecutionServiceAdapter(final TaskExecutor taskExecutor,
                                                final OperatorContext operatorContext,
                                                final OperatorContextProvider operatorContextProvider,
                                                final ObjectProvider<DepartmentAssignmentPort> departmentAssignmentPort) {
        this.taskExecutor = taskExecutor;
        this.operatorContext = operatorContext;
        this.operatorContextProvider = operatorContextProvider;
        this.departmentAssignmentPort = departmentAssignmentPort;
    }

    @Override
    public void submit(final DepartmentAssignmentRunId runId) {
        final OperatorId operatorId = this.operatorContextProvider.currentOperatorId();
        try {
            this.taskExecutor.execute(() -> this.operatorContext.runAs(operatorId,
                    () -> this.departmentAssignmentPort.getObject().process(runId)));
        } catch (final TaskRejectedException exception) {
            log.warn("Department assignment run {} was rejected by the executor; the recovery job will re-dispatch it",
                    runId.value());
        }
    }
}
