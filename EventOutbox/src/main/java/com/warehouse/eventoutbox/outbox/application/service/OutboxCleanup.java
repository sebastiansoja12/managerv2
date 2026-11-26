package com.warehouse.eventoutbox.outbox.application.service;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OutboxCleanup {

    private final OutboxEventRepository repository;
    private final Clock clock;
    private final Duration retention;
    private final int batchSize;

    public OutboxCleanup(final OutboxEventRepository repository,
                         final Clock clock,
                         final Duration retention,
                         final int batchSize) {
        this.repository = repository;
        this.clock = clock;
        this.retention = retention;
        this.batchSize = batchSize;
    }

    public void deletePublished() {
        final Instant publishedBefore = this.clock.instant().minus(this.retention);
        long deletedTotal = 0;
        int deleted;
        do {
            deleted = this.repository.deletePublishedBefore(publishedBefore, this.batchSize);
            deletedTotal += deleted;
        } while (deleted == this.batchSize);
        if (deletedTotal > 0) {
            log.info("[outbox] deleted {} published events older than {}", deletedTotal, publishedBefore);
        }
    }
}
