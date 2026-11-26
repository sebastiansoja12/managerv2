package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.logisticsdelivery.application.port.secondary.AssignmentRunMetrics;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;
import io.micrometer.core.instrument.MeterRegistry;

import java.time.Duration;

public class MicrometerAssignmentRunMetrics implements AssignmentRunMetrics {

    private static final String SHIPMENTS = "courier.assignment.shipments";

    private static final String RUNS = "courier.assignment.runs";

    private static final String RUN_DURATION = "courier.assignment.run.duration";

    private static final String DEPARTMENTS = "courier.assignment.orchestrator.departments";

    private static final String RECOVERED = "courier.assignment.recovery.redispatched";

    private final MeterRegistry meterRegistry;

    public MicrometerAssignmentRunMetrics(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void recordRun(final DepartmentAssignmentRunSnapshot run, final Duration duration) {
        final String department = String.valueOf(run.departmentId().getValue());
        final AssignmentRunSummary summary = run.summary();
        increment(department, "discovered", summary.discovered());
        increment(department, "eligible", summary.eligible());
        increment(department, "assigned", summary.assigned());
        increment(department, "no_courier", summary.noCourier());
        increment(department, "skipped_not_at_department", summary.skippedNotAtDepartment());
        increment(department, "skipped_concurrent", summary.skippedConcurrent());
        increment(department, "invalid_destination", summary.invalidDestination());
        increment(department, "failed", summary.failed());
        this.meterRegistry.counter(RUNS, "department", department, "status", run.status().name()).increment();
        this.meterRegistry.timer(RUN_DURATION, "department", department).record(duration);
    }

    @Override
    public void recordOrchestration(final int triggeredDepartments, final int skippedDepartments) {
        this.meterRegistry.counter(DEPARTMENTS, "result", "triggered").increment(triggeredDepartments);
        this.meterRegistry.counter(DEPARTMENTS, "result", "skipped").increment(skippedDepartments);
    }

    @Override
    public void recordRecovery(final int redispatchedRuns) {
        this.meterRegistry.counter(RECOVERED).increment(redispatchedRuns);
    }

    private void increment(final String department, final String result, final long amount) {
        this.meterRegistry.counter(SHIPMENTS, "department", department, "result", result).increment(amount);
    }
}
