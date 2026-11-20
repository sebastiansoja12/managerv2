package com.warehouse.eventoutbox.consumption;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Supplier;

import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.Headers;

import com.warehouse.eventoutbox.integration.registry.IntegrationEventDefinition;
import com.warehouse.eventoutbox.integration.registry.IntegrationEventRegistry;
import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;

public class IntegrationEventTypeResolver {

    private static final int DEFAULT_EVENT_VERSION = 1;

    private final Supplier<IntegrationEventRegistry> registry;

    public IntegrationEventTypeResolver(final Supplier<IntegrationEventRegistry> registry) {
        this.registry = registry;
    }

    public Optional<IntegrationEventDefinition<?>> resolve(final Headers headers) {
        final IntegrationEventRegistry currentRegistry = this.registry.get();
        if (currentRegistry == null) {
            return Optional.empty();
        }
        final int version = header(headers, KafkaEventHeaders.EVENT_VERSION)
                .map(Integer::valueOf)
                .orElse(DEFAULT_EVENT_VERSION);
        return header(headers, KafkaEventHeaders.CE_TYPE)
                .or(() -> header(headers, KafkaEventHeaders.EVENT_TYPE))
                .flatMap(type -> currentRegistry.findByType(type, version))
                .or(() -> header(headers, KafkaEventHeaders.TYPE_ID).flatMap(currentRegistry::findByClassName));
    }

    private Optional<String> header(final Headers headers, final String name) {
        final Header header = headers.lastHeader(name);
        if (header == null || header.value() == null) {
            return Optional.empty();
        }
        return Optional.of(new String(header.value(), StandardCharsets.UTF_8));
    }
}
