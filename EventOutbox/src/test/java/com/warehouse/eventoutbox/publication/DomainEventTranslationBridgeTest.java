package com.warehouse.eventoutbox.publication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.domain.model.DomainEvent;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

class DomainEventTranslationBridgeTest {

    private static final Instant NOW = Instant.parse("2026-11-19T10:00:00Z");

    private final List<IntegrationEvent> published = new ArrayList<>();

    @Test
    void shouldUseMostSpecificTranslationOnly() {
        final DomainEventTranslationBridge bridge = bridge(new ShipmentTranslator());

        bridge.onDomainEvent(new ShipmentCanceled());

        assertThat(this.published).extracting(IntegrationEvent::getClass)
                .containsExactly(StatusChangedIntegrationEvent.class, CanceledIntegrationEvent.class);
    }

    @Test
    void shouldFallBackToTranslationOfSuperclass() {
        final DomainEventTranslationBridge bridge = bridge(new ShipmentTranslator());

        bridge.onDomainEvent(new ShipmentCollected());

        assertThat(this.published).extracting(IntegrationEvent::getClass)
                .containsExactly(StatusChangedIntegrationEvent.class);
    }

    @Test
    void shouldPublishNothingWhenTranslationReturnsEmptyList() {
        final DomainEventTranslationBridge bridge = bridge(new ShipmentTranslator());

        bridge.onDomainEvent(new ShipmentReturnCompleted());

        assertThat(this.published).isEmpty();
    }

    @Test
    void shouldPublishNothingWhenNoTranslationMatches() {
        final DomainEventTranslationBridge bridge = bridge(new ShipmentTranslator());

        bridge.onDomainEvent(new NoteAdded());

        assertThat(this.published).isEmpty();
    }

    @Test
    void shouldInvokeEveryTranslatorBean() {
        final DomainEventTranslationBridge bridge = bridge(new ShipmentTranslator(), new AuditTranslator());

        bridge.onDomainEvent(new ShipmentCollected());

        assertThat(this.published).extracting(IntegrationEvent::getClass)
                .containsExactly(StatusChangedIntegrationEvent.class, AuditIntegrationEvent.class);
    }

    @Test
    void shouldThrowExceptionWhenDomainEventIsTranslatedTwiceInOneBean() {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();

        assertThatThrownBy(() -> registry.postProcessAfterInitialization(new DuplicatedTranslator(), "duplicated"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(ShipmentCanceled.class.getName());
    }

    @Test
    void shouldThrowExceptionWhenTranslationDoesNotReturnIntegrationEvents() {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();

        assertThatThrownBy(() -> registry.postProcessAfterInitialization(new InvalidTranslator(), "invalid"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("must return");
    }

    @Test
    void shouldPropagateExceptionThrownByTranslation() {
        final DomainEventTranslationBridge bridge = bridge(new FailingTranslator());

        assertThatThrownBy(() -> bridge.onDomainEvent(new ShipmentCollected()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("snapshot is incomplete");
    }

    private DomainEventTranslationBridge bridge(final Object... translators) {
        final IntegrationEventTranslationRegistry registry = new IntegrationEventTranslationRegistry();
        for (final Object translator : translators) {
            registry.postProcessAfterInitialization(translator, translator.getClass().getSimpleName());
        }
        return new DomainEventTranslationBridge(registry, this.published::add);
    }

    static class ShipmentChanged implements DomainEvent {

        @Override
        public Instant getTimestamp() {
            return NOW;
        }
    }

    static class ShipmentStatusChanged extends ShipmentChanged {
    }

    static class ShipmentCollected extends ShipmentStatusChanged {
    }

    static class ShipmentCanceled extends ShipmentStatusChanged {
    }

    static class ShipmentReturnCompleted extends ShipmentStatusChanged {
    }

    static class NoteAdded implements DomainEvent {

        @Override
        public Instant getTimestamp() {
            return NOW;
        }
    }

    record StatusChangedIntegrationEvent() implements IntegrationEvent {
    }

    record CanceledIntegrationEvent() implements IntegrationEvent {
    }

    record AuditIntegrationEvent() implements IntegrationEvent {
    }

    static class ShipmentTranslator {

        @IntegrationEventTranslation
        public StatusChangedIntegrationEvent translate(final ShipmentStatusChanged event) {
            return new StatusChangedIntegrationEvent();
        }

        @IntegrationEventTranslation
        public List<IntegrationEvent> translate(final ShipmentCanceled event) {
            return List.of(new StatusChangedIntegrationEvent(), new CanceledIntegrationEvent());
        }

        @IntegrationEventTranslation
        public List<IntegrationEvent> translate(final ShipmentReturnCompleted event) {
            return List.of();
        }
    }

    static class AuditTranslator {

        @IntegrationEventTranslation
        public AuditIntegrationEvent translate(final ShipmentChanged event) {
            return new AuditIntegrationEvent();
        }
    }

    static class DuplicatedTranslator {

        @IntegrationEventTranslation
        public CanceledIntegrationEvent first(final ShipmentCanceled event) {
            return new CanceledIntegrationEvent();
        }

        @IntegrationEventTranslation
        public CanceledIntegrationEvent second(final ShipmentCanceled event) {
            return new CanceledIntegrationEvent();
        }
    }

    static class InvalidTranslator {

        @IntegrationEventTranslation
        public String translate(final ShipmentCanceled event) {
            return "not an event";
        }
    }

    static class FailingTranslator {

        @IntegrationEventTranslation
        public StatusChangedIntegrationEvent translate(final ShipmentStatusChanged event) {
            throw new IllegalArgumentException("snapshot is incomplete");
        }
    }
}
