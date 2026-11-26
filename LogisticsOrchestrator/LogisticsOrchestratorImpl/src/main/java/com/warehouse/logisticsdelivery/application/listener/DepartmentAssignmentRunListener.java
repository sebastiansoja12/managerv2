package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunExecutionServicePort;
import com.warehouse.logisticsdelivery.domain.event.DepartmentAssignmentRunCreated;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

public class DepartmentAssignmentRunListener {

    private final AssignmentRunExecutionServicePort executionServicePort;

    public DepartmentAssignmentRunListener(final AssignmentRunExecutionServicePort executionServicePort) {
        this.executionServicePort = executionServicePort;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT, fallbackExecution = true)
    public void handle(final DepartmentAssignmentRunCreated event) {
        this.executionServicePort.submit(event.runId());
    }
}
