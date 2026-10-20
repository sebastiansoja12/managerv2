package com.warehouse.asyncjob.application.port.primary;

import com.warehouse.asyncjob.application.port.primary.command.CreateReadModelRebuildJobCommand;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.application.port.secondary.AsyncJobSchedulerServicePort;
import com.warehouse.asyncjob.application.service.AsyncJobExecutionService;
import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.exception.AsyncJobNotFoundException;
import com.warehouse.asyncjob.domain.vo.AsyncJobId;
import com.warehouse.asyncjob.domain.vo.AsyncJobSnapshot;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AsyncJobPortImplTest {

    private AsyncJobRepository asyncJobRepository;
    private AsyncJobSchedulerServicePort schedulerServicePort;
    private AsyncJobExecutionService executionService;
    private AsyncJobPortImpl asyncJobPort;

    @BeforeEach
    void setUp() {
        this.asyncJobRepository = mock(AsyncJobRepository.class);
        this.schedulerServicePort = mock(AsyncJobSchedulerServicePort.class);
        this.executionService = mock(AsyncJobExecutionService.class);
        this.asyncJobPort = new AsyncJobPortImpl(this.asyncJobRepository, this.schedulerServicePort,
                this.executionService);
        when(this.asyncJobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldCreatePendingJobAndScheduleExecutionAfterCommit() {
        final AsyncJobSnapshot job = this.asyncJobPort.createReadModelRebuildJob(new CreateReadModelRebuildJobCommand(
                ReadModelType.DELIVERY, OperatorId.of(7L), LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 20)));

        assertThat(job.status()).isEqualTo(AsyncJobStatus.PENDING);
        final ArgumentCaptor<Runnable> execution = ArgumentCaptor.forClass(Runnable.class);
        verify(this.schedulerServicePort).scheduleAfterCommit(execution.capture());
        execution.getValue().run();
        verify(this.executionService).execute(job.id());
    }

    @Test
    void shouldThrowExceptionWhenJobDoesNotExist() {
        final AsyncJobId jobId = AsyncJobId.generate();
        when(this.asyncJobRepository.findById(jobId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> this.asyncJobPort.get(jobId)).isInstanceOf(AsyncJobNotFoundException.class);
    }

    @Test
    void shouldLimitLatestJobsTo100() {
        when(this.asyncJobRepository.findLatestByOperatorId(OperatorId.of(7L), 100)).thenReturn(List.of());

        this.asyncJobPort.findLatest(OperatorId.of(7L), 500);

        verify(this.asyncJobRepository).findLatestByOperatorId(OperatorId.of(7L), 100);
    }
}
