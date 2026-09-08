package com.warehouse.asyncjob.domain;

import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AsyncJobTest {

    @Test
    void shouldCreatePendingJob() {
        final AsyncJobSnapshot job = pendingJob().snapshot();

        assertThat(job.status()).isEqualTo(AsyncJobStatus.PENDING);
        assertThat(job.createdAt()).isNotNull();
        assertThat(job.startedAt()).isNull();
    }

    @Test
    void shouldCompleteRunningJob() {
        final AsyncJob job = pendingJob();

        job.markRunning();
        job.markCompleted();

        final AsyncJobSnapshot snapshot = job.snapshot();
        assertThat(snapshot.status()).isEqualTo(AsyncJobStatus.COMPLETED);
        assertThat(snapshot.startedAt()).isNotNull();
        assertThat(snapshot.finishedAt()).isNotNull();
    }

    @Test
    void shouldStoreFailureReasonWhenJobFails() {
        final AsyncJob job = pendingJob();
        job.markRunning();

        job.markFailed("Rebuild failed");

        assertThat(job.snapshot().status()).isEqualTo(AsyncJobStatus.FAILED);
        assertThat(job.snapshot().error()).isEqualTo("Rebuild failed");
    }

    private AsyncJob pendingJob() {
        return AsyncJob.pending(ReadModelType.DELIVERY, OperatorId.of(7L), LocalDate.of(2026, 10, 1),
                LocalDate.of(2026, 10, 20));
    }
}
