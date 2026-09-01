package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.application.KafkaOutboxPublicationService;
import com.warehouse.commonassets.kafka.application.TransactionalKafkaOutboxWriter;
import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TransactionalKafkaOutboxWriterTest {

    @Mock
    private KafkaOutboxPort outboxPort;

    @Mock
    private KafkaOutboxPublicationService publicationService;

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

        verifyNoInteractions(this.outboxPort, this.publicationService);
    }

    @Test
    void shouldStoreRecordInTransactionAndPublishAfterCommit() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        beginTransaction();

        writer.append(record);

        verify(this.outboxPort).save(record);
        verify(this.publicationService, never()).publish(record);

        completeTransaction(TransactionSynchronization.STATUS_COMMITTED);

        verify(this.publicationService).publish(record);
    }

    @Test
    void shouldNotPublishAfterRollback() {
        final TransactionalKafkaOutboxWriter writer = writer();
        final KafkaOutboxRecord record = record();
        beginTransaction();

        writer.append(record);
        completeTransaction(TransactionSynchronization.STATUS_ROLLED_BACK);

        verify(this.outboxPort).save(record);
        verify(this.publicationService, never()).publish(record);
    }

    private TransactionalKafkaOutboxWriter writer() {
        return new TransactionalKafkaOutboxWriter(this.outboxPort, this.publicationService);
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
