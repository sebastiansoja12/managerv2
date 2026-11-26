package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.entity.OutboxEventEntity;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventEntityMapper;
import com.warehouse.eventoutbox.integration.model.EventId;

public class OutboxEventRepositoryImpl implements OutboxEventRepository {

    private final OutboxEventJpaRepository jpaRepository;
    private final OutboxEventEntityMapper mapper;

    public OutboxEventRepositoryImpl(final OutboxEventJpaRepository jpaRepository,
                                     final OutboxEventEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(final OutboxEvent outboxEvent) {
        this.jpaRepository.save(this.mapper.toEntity(outboxEvent));
    }

    @Override
    public void saveAll(final List<OutboxEvent> outboxEvents) {
        if (outboxEvents.isEmpty()) {
            return;
        }
        final Map<EventId, OutboxEventEntity> entitiesByEventId = this.jpaRepository
                .findAllByEventIdIn(outboxEvents.stream().map(OutboxEvent::getEventId).toList())
                .stream()
                .collect(Collectors.toMap(OutboxEventEntity::getEventId, Function.identity()));
        this.jpaRepository.saveAll(outboxEvents.stream()
                .map(outboxEvent -> this.mapper.withState(existingEntity(entitiesByEventId, outboxEvent), outboxEvent))
                .toList());
    }

    private OutboxEventEntity existingEntity(final Map<EventId, OutboxEventEntity> entitiesByEventId,
                                             final OutboxEvent outboxEvent) {
        final OutboxEventEntity entity = entitiesByEventId.get(outboxEvent.getEventId());
        if (entity == null) {
            throw new IllegalStateException("Outbox event does not exist: " + outboxEvent.getEventId().value());
        }
        return entity;
    }

    @Override
    public List<OutboxEvent> findPublishable(final Instant now, final int limit) {
        return this.jpaRepository.findPublishable(OutboxEventStatus.PENDING, OutboxEventStatus.DEAD, now,
                        PageRequest.of(0, limit))
                .stream()
                .map(this.mapper::toDomain)
                .toList();
    }

    @Override
    public int deletePublishedBefore(final Instant publishedBefore, final int limit) {
        final List<Long> sequenceNumbers = this.jpaRepository.findSequenceNumbersPublishedBefore(
                OutboxEventStatus.PUBLISHED, publishedBefore, PageRequest.of(0, limit));
        if (sequenceNumbers.isEmpty()) {
            return 0;
        }
        this.jpaRepository.deleteAllByIdInBatch(sequenceNumbers);
        return sequenceNumbers.size();
    }

    @Override
    public long countByStatus(final OutboxEventStatus status) {
        return this.jpaRepository.countByStatus(status);
    }

    @Override
    public Optional<Instant> findOldestPendingOccurredAt() {
        return Optional.ofNullable(this.jpaRepository.findOldestOccurredAt(OutboxEventStatus.PENDING));
    }
}
