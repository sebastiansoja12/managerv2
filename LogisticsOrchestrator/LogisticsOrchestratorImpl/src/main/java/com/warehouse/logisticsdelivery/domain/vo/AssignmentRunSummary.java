package com.warehouse.logisticsdelivery.domain.vo;

public record AssignmentRunSummary(long discovered,
                                   long eligible,
                                   long assigned,
                                   long noCourier,
                                   long skippedNotAtDepartment,
                                   long skippedConcurrent,
                                   long invalidDestination,
                                   long failed,
                                   int batches) {

    public static AssignmentRunSummary empty() {
        return new AssignmentRunSummary(0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public AssignmentRunSummary plus(final AssignmentRunSummary batch) {
        return new AssignmentRunSummary(discovered + batch.discovered, eligible + batch.eligible,
                assigned + batch.assigned, noCourier + batch.noCourier,
                skippedNotAtDepartment + batch.skippedNotAtDepartment, skippedConcurrent + batch.skippedConcurrent,
                invalidDestination + batch.invalidDestination, failed + batch.failed, batches + batch.batches);
    }
}
