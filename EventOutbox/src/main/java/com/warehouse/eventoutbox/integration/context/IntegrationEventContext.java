package com.warehouse.eventoutbox.integration.context;

import java.util.Map;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import tools.jackson.databind.node.ObjectNode;

/**
 * Extension point of the publishing service: contributes its own context (tenant, operator, user...) to every
 * published integration event. Each service provides at most one bean; without it events carry no context.
 */
public interface IntegrationEventContext {

    IntegrationEventContext NONE = event -> Map.of();

    /**
     * Attributes stored as message headers of the given event.
     */
    Map<String, String> attributes(IntegrationEvent event);

    /**
     * Legacy envelope only: copies the attributes into the serialized payload for consumers that still read them
     * from the message body.
     */
    default void enrichLegacyPayload(final ObjectNode payload, final Map<String, String> attributes) {
    }
}
