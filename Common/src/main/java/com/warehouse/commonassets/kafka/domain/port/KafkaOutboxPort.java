package com.warehouse.commonassets.kafka.domain.port;

import java.util.List;
import java.util.UUID;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;

public interface KafkaOutboxPort {

    void save(KafkaOutboxRecord record);

    List<KafkaOutboxRecord> findUnpublished(int limit);

    void markPublished(UUID eventId);

    void markFailed(UUID eventId, Throwable exception);
}
