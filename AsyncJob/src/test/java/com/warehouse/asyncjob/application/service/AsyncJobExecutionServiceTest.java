package com.warehouse.asyncjob.application.service;

import com.warehouse.asyncjob.application.port.secondary.AsyncJobRepository;
import com.warehouse.asyncjob.application.port.secondary.ReadModelRebuildServicePort;
import com.warehouse.asyncjob.domain.enumeration.AsyncJobStatus;
import com.warehouse.asyncjob.domain.enumeration.ReadModelType;
import com.warehouse.asyncjob.domain.model.AsyncJob;
import com.warehouse.commonassets.identificator.OperatorId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.SimpleTransactionStatus;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AsyncJobExecutionServiceTest {

    private static final LocalDate FROM = LocalDate.of(2026, 10, 1);
    private static final LocalDate TO = LocalDate.of(2026, 10, 20);

    private AsyncJobRepository asyncJobRepository;
    private ReadModelRebuildServicePort rebuildServicePort;
    private AsyncJobExecutionService executionService;
    private AsyncJob job;

    @BeforeEach
    void setUp() {
        this.asyncJobRepository = mock(AsyncJobRepository.class);
        this.rebuildServicePort = mock(ReadModelRebuildServicePort.class);
        when(this.rebuildServicePort.type()).thenReturn(ReadModelType.DELIVERY);
        final PlatformTransactionManager transactionManager = mock(PlatformTransactionManager.class);
        when(transactionManager.getTransaction(any())).thenReturn(new SimpleTransactionStatus());
        this.executionService = new AsyncJobExecutionService(this.asyncJobRepository,
                new ReadModelRebuildServiceRegistry(List.of(this.rebuildServicePort)),
                new TransactionTemplate(transactionManager));
        this.job = AsyncJob.pending(ReadModelType.DELIVERY, OperatorId.of(7L), FROM, TO);
        when(this.asyncJobRepository.findById(this.job.getId())).thenReturn(Optional.of(this.job));
        when(this.asyncJobRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void shouldRebuildReadModelAndCompleteJob() {
        this.executionService.execute(this.job.getId());

        verify(this.rebuildServicePort).rebuild(OperatorId.of(7L), FROM, TO);
        assertThat(this.job.snapshot().status()).isEqualTo(AsyncJobStatus.COMPLETED);
    }

    @Test
    void shouldMarkJobAsFailedWhenRebuildFails() {
        doThrow(new IllegalStateException("Rebuild failed")).when(this.rebuildServicePort)
                .rebuild(OperatorId.of(7L), FROM, TO);

        this.executionService.execute(this.job.getId());

        assertThat(this.job.snapshot().status()).isEqualTo(AsyncJobStatus.FAILED);
        assertThat(this.job.snapshot().error()).isEqualTo("Rebuild failed");
    }
}
