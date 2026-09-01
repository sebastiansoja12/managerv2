package com.warehouse.commonassets.kafka.domain.port;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.warehouse.commonassets.kafka.domain.model.KafkaOutboxRecord;

public interface KafkaOutboxPort {

    void save(KafkaOutboxRecord record);

    List<KafkaOutboxRecord> claimPending(int limit, String workerId, Instant now, Instant lockedUntil);

    boolean claim(UUID eventId, String workerId, Instant now, Instant lockedUntil);

    void markPublished(UUID eventId, String workerId);

    void markFailed(UUID eventId,
                    String workerId,
                    Throwable exception,
                    Instant nextAttemptAt,
                    int maxAttempts);
}
