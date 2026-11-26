package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentAssignmentRunId;
import com.warehouse.logisticsdelivery.application.port.primary.result.AssignmentOrchestration;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunExecutionServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunMetrics;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentAssignmentRunRepository;
import com.warehouse.logisticsdelivery.application.service.AssignmentOrchestrator;
import com.warehouse.logisticsdelivery.application.service.AssignmentRunProcessor;
import com.warehouse.logisticsdelivery.domain.model.DepartmentAssignmentRun;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionOperations;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class DepartmentAssignmentPortImpl implements DepartmentAssignmentPort {

    private static final Logger log = LoggerFactory.getLogger(DepartmentAssignmentPortImpl.class);

    private final AssignmentOrchestrator orchestrator;

    private final AssignmentRunProcessor processor;

    private final DepartmentAssignmentRunRepository runRepository;

    private final AssignmentRunExecutionServicePort executionServicePort;

    private final AssignmentRunMetrics metrics;

    private final TransactionOperations transactionOperations;

    private final Clock clock;

    private final Duration pendingRecoveryDelay;

    private final Duration retention;

    private final int recoveryLimit;

    public DepartmentAssignmentPortImpl(final AssignmentOrchestrator orchestrator,
                                        final AssignmentRunProcessor processor,
                                        final DepartmentAssignmentRunRepository runRepository,
                                        final AssignmentRunExecutionServicePort executionServicePort,
                                        final AssignmentRunMetrics metrics,
                                        final TransactionOperations transactionOperations,
                                        final Clock clock,
                                        final Duration pendingRecoveryDelay,
                                        final Duration retention,
                                        final int recoveryLimit) {
        this.orchestrator = orchestrator;
        this.processor = processor;
        this.runRepository = runRepository;
        this.executionServicePort = executionServicePort;
        this.metrics = metrics;
        this.transactionOperations = transactionOperations;
        this.clock = clock;
        this.pendingRecoveryDelay = pendingRecoveryDelay;
        this.retention = retention;
        this.recoveryLimit = recoveryLimit;
    }

    @Override
    public AssignmentOrchestration orchestrate() {
        final AssignmentOrchestration orchestration = this.orchestrator.orchestrate();
        this.metrics.recordOrchestration(orchestration.triggeredDepartments(),
                orchestration.configuredDepartments() - orchestration.triggeredDepartments());
        return orchestration;
    }

    @Override
    public void process(final DepartmentAssignmentRunId runId) {
        this.processor.process(runId);
    }

    @Override
    public int recover() {
        final Instant now = this.clock.instant();
        final List<DepartmentAssignmentRunId> recoverable = Objects.requireNonNull(this.transactionOperations.execute(
                status -> this.runRepository.findRecoverable(now.minus(this.pendingRecoveryDelay), now,
                        this.recoveryLimit)));
        recoverable.forEach(runId -> {
            log.warn("Re-dispatching department assignment run {} from its saved cursor", runId.value());
            this.executionServicePort.submit(runId);
        });
        this.metrics.recordRecovery(recoverable.size());
        return recoverable.size();
    }

    @Override
    public int cleanup() {
        final int deleted = Objects.requireNonNull(this.transactionOperations.execute(status ->
                this.runRepository.deleteFinishedBefore(this.clock.instant().minus(this.retention))));
        log.info("Deleted {} finished department assignment runs older than {}", deleted, this.retention);
        return deleted;
    }

    @Override
    public List<DepartmentAssignmentRunSnapshot> findLatestRuns(final int limit) {
        return this.runRepository.findLatest(limit).stream()
                .map(DepartmentAssignmentRun::snapshot)
                .toList();
    }
}
