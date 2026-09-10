package com.warehouse.commonassets.kafka.application;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@ConditionalOnProperty(name = "manager.kafka.outbox.enabled", havingValue = "true")
public class TransactionalKafkaOutboxWriter {

    private final KafkaOutboxPort outboxPort;
    private final KafkaOutboxPublicationService outboxPublicationService;

    public TransactionalKafkaOutboxWriter(final KafkaOutboxPort outboxPort,
                                          final KafkaOutboxPublicationService outboxPublicationService) {
        this.outboxPort = outboxPort;
        this.outboxPublicationService = outboxPublicationService;
    }

    public void append(final KafkaOutboxRecord record) {
        requireActiveTransaction();
        this.outboxPort.save(record);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                outboxPublicationService.publish(record);
            }
        });
    }

    private void requireActiveTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("Kafka outbox record must be stored in an active transaction");
        }
    }
}
