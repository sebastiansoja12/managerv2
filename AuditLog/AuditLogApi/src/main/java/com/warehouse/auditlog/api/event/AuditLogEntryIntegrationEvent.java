package com.warehouse.auditlog.api.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.auditlog.api.event.snapshot.AuditLogEntryData;
import com.warehouse.commonassets.event.integration.annotation.IntegrationEventType;
import com.warehouse.commonassets.event.integration.context.OperatorAwareContext;
import com.warehouse.commonassets.event.integration.model.IntegrationEvent;
import com.warehouse.commonassets.event.integration.model.IntegrationEventKey;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "audit-log.entry", version = 1)
public final class AuditLogEntryIntegrationEvent extends OperatorAwareContext
        implements IntegrationEvent, IntegrationEventKey {

    private final AuditLogEntryData payload;

    private final UUID eventId;

    public AuditLogEntryIntegrationEvent(final AuditLogEntryData payload) {
        this(payload, null);
    }

    @JsonCreator
    public AuditLogEntryIntegrationEvent(@JsonProperty("payload") final AuditLogEntryData payload,
                                         @JsonProperty("eventId") final UUID eventId) {
        this.payload = Objects.requireNonNull(payload, "Audit log entry data cannot be null");
        this.eventId = eventId;
    }

    @JsonProperty("payload")
    public AuditLogEntryData payload() {
        return this.payload;
    }

    public Optional<UUID> eventId() {
        return Optional.ofNullable(this.eventId);
    }

    @Override
    public String eventKey() {
        return this.payload.resourceType().name() + ":" + this.payload.resourceId().value();
    }
}
