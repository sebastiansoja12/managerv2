package com.warehouse.eventoutbox.consumption;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import com.warehouse.eventoutbox.integration.model.EventMetadata;
import com.warehouse.eventoutbox.integration.model.EventId;
import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;

public class EventMetadataArgumentResolver implements HandlerMethodArgumentResolver {

    private static final int DEFAULT_EVENT_VERSION = 1;
    private static final String CLOUD_EVENTS_PREFIX = "ce_";
    private static final String SPRING_KAFKA_PREFIX = "kafka_";
    private static final Set<String> ENVELOPE_HEADERS = Set.of(KafkaEventHeaders.TYPE_ID, KafkaEventHeaders.EVENT_ID,
            KafkaEventHeaders.EVENT_TYPE, KafkaEventHeaders.EVENT_CLASS, KafkaEventHeaders.EVENT_VERSION,
            KafkaEventHeaders.OCCURRED_AT, KafkaEventHeaders.TRACEPARENT, KafkaEventHeaders.TRACESTATE);

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return EventMetadata.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final Message<?> message) {
        final MessageHeaders headers = message.getHeaders();
        return new EventMetadata(
                header(headers, KafkaEventHeaders.CE_ID).or(() -> header(headers, KafkaEventHeaders.EVENT_ID))
                        .map(UUID::fromString)
                        .map(EventId::new)
                        .orElse(null),
                header(headers, KafkaEventHeaders.CE_TYPE).or(() -> header(headers, KafkaEventHeaders.EVENT_TYPE))
                        .orElse(null),
                header(headers, KafkaEventHeaders.EVENT_VERSION).map(Integer::valueOf).orElse(DEFAULT_EVENT_VERSION),
                header(headers, KafkaEventHeaders.CE_TIME).or(() -> header(headers, KafkaEventHeaders.OCCURRED_AT))
                        .map(Instant::parse)
                        .orElse(null),
                header(headers, KafkaEventHeaders.CE_SOURCE).orElse(null),
                header(headers, KafkaEventHeaders.CE_SUBJECT).orElse(null),
                attributes(headers));
    }

    private Map<String, String> attributes(final MessageHeaders headers) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        headers.forEach((name, value) -> {
            if (name.startsWith(CLOUD_EVENTS_PREFIX) || name.startsWith(SPRING_KAFKA_PREFIX)
                    || ENVELOPE_HEADERS.contains(name)) {
                return;
            }
            if (value instanceof final byte[] bytes) {
                attributes.put(name, new String(bytes, StandardCharsets.UTF_8));
            } else if (value instanceof final String text) {
                attributes.put(name, text);
            }
        });
        return attributes;
    }

    private Optional<String> header(final MessageHeaders headers, final String name) {
        final Object value = headers.get(name);
        if (value instanceof final byte[] bytes) {
            return Optional.of(new String(bytes, StandardCharsets.UTF_8));
        }
        return Optional.ofNullable(value).map(String::valueOf);
    }
}
