package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxEventRepository;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.domain.service.OutboxEventFactory;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventHeadersMapper;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.mapper.OutboxEventPayloadMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IntegrationEventOutboxAdapter implements IntegrationEventPublisher {

    private final IntegrationEventRegistry registry;
    private final OutboxEventRepository repository;
    private final EventMetadataFactory metadataFactory;
    private final OutboxEventHeadersMapper headersMapper;
    private final OutboxEventPayloadMapper payloadMapper;
    private final OutboxEventFactory outboxEventFactory;
    private final OutboxTracing tracing;
    private final Runnable relayWakeUp;

    public IntegrationEventOutboxAdapter(final IntegrationEventRegistry registry,
                                         final OutboxEventRepository repository,
                                         final EventMetadataFactory metadataFactory,
                                         final OutboxEventHeadersMapper headersMapper,
                                         final OutboxEventPayloadMapper payloadMapper,
                                         final OutboxEventFactory outboxEventFactory,
                                         final OutboxTracing tracing,
                                         final Runnable relayWakeUp) {
        this.registry = registry;
        this.repository = repository;
        this.metadataFactory = metadataFactory;
        this.headersMapper = headersMapper;
        this.payloadMapper = payloadMapper;
        this.outboxEventFactory = outboxEventFactory;
        this.tracing = tracing;
        this.relayWakeUp = relayWakeUp;
    }

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public void publish(final IntegrationEvent event) {
        final IntegrationEventDefinition<?> definition = this.registry.definitionOf(event.getClass());
        final EventMetadata metadata = this.metadataFactory.create(definition, event);
        final Map<String, String> headers = new LinkedHashMap<>(this.headersMapper.map(metadata, event.getClass()));
        headers.putAll(this.tracing.currentContext());
        this.repository.save(this.outboxEventFactory.create(
                metadata,
                definition.topic(),
                this.payloadMapper.map(event, metadata),
                headers));
        wakeUpRelayAfterCommit();
        log.debug("[outbox] stored eventId={} type={} topic={} key={}",
                metadata.eventId().value(), metadata.eventType(), definition.topic(), metadata.subject());
    }

    private void wakeUpRelayAfterCommit() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                IntegrationEventOutboxAdapter.this.relayWakeUp.run();
            }
        });
    }
}
