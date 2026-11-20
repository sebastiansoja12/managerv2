package com.warehouse.eventoutbox.publication;

import java.util.List;

import org.springframework.context.event.EventListener;

import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DomainEventTranslationBridge {

    private final IntegrationEventTranslationRegistry translationRegistry;
    private final IntegrationEventPublisher integrationEventPublisher;

    public DomainEventTranslationBridge(final IntegrationEventTranslationRegistry translationRegistry,
                                        final IntegrationEventPublisher integrationEventPublisher) {
        this.translationRegistry = translationRegistry;
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @EventListener
    public void onDomainEvent(final DomainEvent event) {
        final List<IntegrationEvent> integrationEvents = this.translationRegistry.translate(event);
        if (integrationEvents.isEmpty()) {
            return;
        }
        log.debug("[translation] {} -> {}", event.getClass().getSimpleName(),
                integrationEvents.stream().map(integrationEvent -> integrationEvent.getClass().getSimpleName()).toList());
        integrationEvents.forEach(this.integrationEventPublisher::publish);
    }
}
