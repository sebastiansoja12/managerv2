package com.warehouse.eventoutbox.integration.model;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

public record EventMetadata(EventId eventId,
                            String eventType,
                            int eventVersion,
                            Instant occurredAt,
                            String source,
                            String subject,
                            Map<String, String> attributes) {

    public EventMetadata {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }

    public Optional<String> attribute(final String name) {
        return Optional.ofNullable(this.attributes.get(name));
    }
}
