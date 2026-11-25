package com.warehouse.eventoutbox.integration.context;

import java.util.Map;

import com.warehouse.eventoutbox.integration.model.IntegrationEvent;

import tools.jackson.databind.node.ObjectNode;

public interface IntegrationEventContext {

    IntegrationEventContext NONE = event -> Map.of();

    Map<String, String> attributes(IntegrationEvent event);

    default void enrichLegacyPayload(final ObjectNode payload, final Map<String, String> attributes) {
    }
}
