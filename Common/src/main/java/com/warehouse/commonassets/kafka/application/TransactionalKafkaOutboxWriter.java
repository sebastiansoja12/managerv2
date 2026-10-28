package com.warehouse.commonassets.kafka.application;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;
import com.warehouse.commonassets.kafka.domain.port.KafkaOutboxPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@ConditionalOnProperty(name = "manager.kafka.outbox.enabled", havingValue = "true")
public class TransactionalKafkaOutboxWriter {

    private final KafkaOutboxPort outboxPort;
    private final KafkaOutboxPublicationService publicationService;
    private final TaskExecutor taskExecutor;

    public TransactionalKafkaOutboxWriter(final KafkaOutboxPort outboxPort,
                                          final KafkaOutboxPublicationService publicationService,
                                          @Qualifier("kafkaOutboxTaskExecutor") final TaskExecutor taskExecutor) {
        this.outboxPort = outboxPort;
        this.publicationService = publicationService;
        this.taskExecutor = taskExecutor;
    }

    @Transactional
    public void append(final KafkaOutboxRecord record) {
        requireActiveTransaction();
        this.outboxPort.save(record);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                schedulePublication(record);
            }
        });
    }

    private void schedulePublication(final KafkaOutboxRecord record) {
        try {
            this.taskExecutor.execute(() -> publish(record));
        } catch (final TaskRejectedException exception) {
            log.warn("Immediate publication of outbox event {} was rejected; scheduled publication will retry",
                    record.eventId());
        }
    }

    private void publish(final KafkaOutboxRecord record) {
        try {
            this.publicationService.publish(record);
        } catch (final RuntimeException exception) {
            log.error("Immediate publication of outbox event {} failed; scheduled publication will retry",
                    record.eventId(), exception);
        }
    }

    private void requireActiveTransaction() {
        if (!TransactionSynchronizationManager.isActualTransactionActive()
                || !TransactionSynchronizationManager.isSynchronizationActive()) {
            throw new IllegalStateException("Kafka outbox record must be stored in an active transaction");
        }
    }
}
