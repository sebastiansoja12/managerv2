package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.application.TransactionalKafkaOutboxWriter;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import com.warehouse.commonassets.kafka.infrastructure.config.KafkaConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.ArgumentCaptor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionalKafkaOutboxWriterTest {

    @Mock
    private KafkaOutboxPort outboxPort;

    @Mock
    private KafkaOutboxPublicationService publicationService;

    @Mock
    private TaskExecutor taskExecutor;

    @AfterEach
    void cleanTransactionState() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
        TransactionSynchronizationManager.setActualTransactionActive(false);
    }

    @Test
    void shouldRejectOutboxWriteWithoutActiveTransaction() {
        final TransactionalKafkaOutboxWriter writer = writer();

        assertThatThrownBy(() -> writer.append(record()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Kafka outbox record must be stored in an active transaction");

        verifyNoInteractions(this.outboxPort, this.publicationService, this.taskExecutor);
    }

    @Test
    void shouldPublishOnlyFromBackgroundTaskScheduledAfterCommit() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        beginTransaction();

        writer.append(record);

        verify(this.outboxPort).save(record);
        verifyNoInteractions(this.publicationService, this.taskExecutor);

        completeTransaction(TransactionSynchronization.STATUS_COMMITTED);

        final ArgumentCaptor<Runnable> publication = ArgumentCaptor.forClass(Runnable.class);
        verify(this.taskExecutor).execute(publication.capture());
        verifyNoInteractions(this.publicationService);

        publication.getValue().run();

        verify(this.publicationService).publish(record);
    }

    @Test
    void shouldPublishOnSeparateThreadWithoutRequestTransaction() throws InterruptedException {
        final ThreadPoolTaskExecutor executor = new KafkaConfiguration().kafkaOutboxTaskExecutor();
        final CountDownLatch published = new CountDownLatch(1);
        final AtomicReference<Thread> publicationThread = new AtomicReference<>();
        final AtomicBoolean transactionActive = new AtomicBoolean(true);
        final KafkaOutboxRecord record = record();
        final TransactionalKafkaOutboxWriter writer =
                new TransactionalKafkaOutboxWriter(this.outboxPort, this.publicationService, executor);
        doAnswer(invocation -> {
            publicationThread.set(Thread.currentThread());
            transactionActive.set(TransactionSynchronizationManager.isActualTransactionActive());
            published.countDown();
            return null;
        }).when(this.publicationService).publish(record);
        executor.initialize();

        try {
            beginTransaction();
            writer.append(record);
            completeTransaction(TransactionSynchronization.STATUS_COMMITTED);

            assertThat(published.await(5, TimeUnit.SECONDS)).isTrue();
            assertThat(publicationThread.get()).isNotSameAs(Thread.currentThread());
            assertThat(transactionActive.get()).isFalse();
        } finally {
            executor.shutdown();
        }
    }

    @Test
    void shouldRejectOutboxWriteWithoutTransactionSynchronization() {
        final TransactionalKafkaOutboxWriter writer = writer();
        TransactionSynchronizationManager.setActualTransactionActive(true);

        assertThatThrownBy(() -> writer.append(record()))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Kafka outbox record must be stored in an active transaction");

        verifyNoInteractions(this.outboxPort, this.publicationService, this.taskExecutor);
    }

    @Test
    void shouldNotSchedulePublicationAfterRollback() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        beginTransaction();

        writer.append(record);
        completeTransaction(TransactionSynchronization.STATUS_ROLLED_BACK);

        verify(this.outboxPort).save(record);
        verifyNoInteractions(this.publicationService, this.taskExecutor);
    }

    @Test
    void shouldLeaveRecordForScheduledPublicationWhenExecutorRejectsTask() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        doThrow(new TaskRejectedException("Queue is full")).when(this.taskExecutor).execute(any(Runnable.class));
        beginTransaction();
        writer.append(record);

        assertThatCode(() -> completeTransaction(TransactionSynchronization.STATUS_COMMITTED))
                .doesNotThrowAnyException();

        verify(this.outboxPort).save(record);
        verifyNoInteractions(this.publicationService);
    }

    @Test
    void shouldLeaveRecordForRetryWhenBackgroundPublicationFails() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        doThrow(new IllegalStateException("Database unavailable")).when(this.publicationService).publish(record);
        beginTransaction();
        writer.append(record);
        completeTransaction(TransactionSynchronization.STATUS_COMMITTED);
        final ArgumentCaptor<Runnable> publication = ArgumentCaptor.forClass(Runnable.class);
        verify(this.taskExecutor).execute(publication.capture());

        assertThatCode(() -> publication.getValue().run()).doesNotThrowAnyException();

        verify(this.outboxPort).save(record);
    }

    private TransactionalKafkaOutboxWriter writer() {
        return new TransactionalKafkaOutboxWriter(this.outboxPort, this.publicationService, this.taskExecutor);
    }

    private void beginTransaction() {
        TransactionSynchronizationManager.setActualTransactionActive(true);
        TransactionSynchronizationManager.initSynchronization();
    }

    private void completeTransaction(final int status) {
        final List<TransactionSynchronization> synchronizations =
                TransactionSynchronizationManager.getSynchronizations();
        if (status == TransactionSynchronization.STATUS_COMMITTED) {
            synchronizations.forEach(TransactionSynchronization::afterCommit);
        }
        synchronizations.forEach(synchronization -> synchronization.afterCompletion(status));
        TransactionSynchronizationManager.clearSynchronization();
        TransactionSynchronizationManager.setActualTransactionActive(false);
    }

    private KafkaOutboxRecord record() {
        return new KafkaOutboxRecord(
                UUID.randomUUID(),
                "shipment.events",
                "shipment-1",
                "shipment.changed",
                1,
                Instant.now(),
                OperatorId.of(7L),
                "{}",
                Map.of()
        );
    }
}
