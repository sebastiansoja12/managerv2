package com.warehouse.logisticsdelivery.domain.model;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.logisticsdelivery.domain.enumeration.DepartmentAssignmentRunStatus;
import com.warehouse.logisticsdelivery.domain.exception.DepartmentAssignmentRunStateException;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentAssignmentRunSnapshot;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

import static com.warehouse.logisticsdelivery.CourierAssignmentFixture.processingRun;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DepartmentAssignmentRunTest {

    private static final Instant NOW = Instant.parse("2026-11-24T13:00:00Z");
    private static final Duration LEASE = Duration.ofMinutes(5);
    private static final DeliveryAssignmentCursor CURSOR = new DeliveryAssignmentCursor(
            LocalDateTime.of(2026, 11, 24, 9, 15), new DeliveryId("d-2"));
    private static final AssignmentRunSummary BATCH = new AssignmentRunSummary(2, 2, 1, 1, 0, 0, 0, 0, 1);

    @Test
    void shouldAccumulateBatchCountersMoveCursorAndExtendLease() {
        final DepartmentAssignmentRun run = processingRun();

        run.recordBatch(BATCH, CURSOR, Instant.parse("2026-11-24T08:15:00Z"), NOW, LEASE);
        run.recordBatch(BATCH, CURSOR, Instant.parse("2026-11-24T08:00:00Z"), NOW, LEASE);

        final DepartmentAssignmentRunSnapshot snapshot = run.snapshot();
        assertThat(snapshot.summary(), equalTo(new AssignmentRunSummary(4, 4, 2, 2, 0, 0, 0, 0, 2)));
        assertThat(snapshot.cursor(), equalTo(CURSOR));
        assertThat(snapshot.maxEligibleAt(), equalTo(Instant.parse("2026-11-24T08:15:00Z")));
        assertThat(snapshot.leaseUntil(), equalTo(NOW.plus(LEASE)));
    }

    @Test
    void shouldSetWatermarkToMaxEligibleTimestampOnCompletion() {
        final DepartmentAssignmentRun run = processingRun();
        run.recordBatch(BATCH, CURSOR, Instant.parse("2026-11-24T08:15:00Z"), NOW, LEASE);

        run.complete(NOW);

        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.COMPLETED));
        assertThat(run.snapshot().watermark(), equalTo(Instant.parse("2026-11-24T08:15:00Z")));
        assertThat(run.snapshot().leaseOwner(), nullValue());
    }

    @Test
    void shouldSetWatermarkToWindowEndWhenNothingWasEligible() {
        final DepartmentAssignmentRun run = processingRun();

        run.complete(NOW);

        assertThat(run.snapshot().watermark(), equalTo(run.getWindow().to()));
    }

    @Test
    void shouldReturnToPendingAndKeepCursorAfterError() {
        final DepartmentAssignmentRun run = processingRun();
        run.recordBatch(BATCH, CURSOR, null, NOW, LEASE);

        run.registerError("deadlock detected", NOW, 3);

        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.PENDING));
        assertThat(run.getRetries(), is(1));
        assertThat(run.getCursor(), equalTo(CURSOR));
        assertThat(run.snapshot().leaseUntil(), nullValue());
    }

    @Test
    void shouldEndInErrorWhenRetriesAreExhausted() {
        final DepartmentAssignmentRun run = processingRun();

        run.registerError("timeout", NOW, 1);

        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.ERROR));
        assertThat(run.snapshot().error(), equalTo("timeout"));
    }

    @Test
    void shouldStopRunOnBusinessFailure() {
        final DepartmentAssignmentRun run = processingRun();

        run.stop("No couriers", NOW);

        assertThat(run.getStatus(), is(DepartmentAssignmentRunStatus.FAILED));
    }

    @Test
    void shouldRejectBatchWhenRunIsNotProcessing() {
        final DepartmentAssignmentRun run = processingRun();
        run.complete(NOW);

        assertThrows(DepartmentAssignmentRunStateException.class,
                () -> run.recordBatch(BATCH, CURSOR, null, NOW, LEASE));
    }
}
