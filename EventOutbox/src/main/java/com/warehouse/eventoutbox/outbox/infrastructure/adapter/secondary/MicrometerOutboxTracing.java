package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import java.util.LinkedHashMap;
import java.util.Map;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;

public class MicrometerOutboxTracing implements OutboxTracing {

    private static final String SEND_SPAN_NAME = "outbox publish";

    private final Tracer tracer;
    private final Propagator propagator;

    public MicrometerOutboxTracing(final Tracer tracer, final Propagator propagator) {
        this.tracer = tracer;
        this.propagator = propagator;
    }

    @Override
    public Map<String, String> currentContext() {
        final Span span = this.tracer.currentSpan();
        if (span == null) {
            return Map.of();
        }
        final Map<String, String> context = new LinkedHashMap<>();
        this.propagator.inject(span.context(), context, Map::put);
        return context;
    }

    @Override
    public OutboxSendSpan startSend(final OutboxEvent event) {
        final Span span = this.propagator.extract(event.getHeaders(), Map::get)
                .name(SEND_SPAN_NAME)
                .kind(Span.Kind.PRODUCER)
                .tag("messaging.system", "kafka")
                .tag("messaging.destination.name", event.getTopic())
                .tag("messaging.message.id", event.getEventId().value().toString())
                .tag("outbox.event.type", event.getEventType())
                .tag("outbox.attempt", String.valueOf(event.getAttemptCount() + 1))
                .start();
        final Map<String, String> headers = new LinkedHashMap<>(event.getHeaders());
        this.propagator.fields().forEach(headers::remove);
        this.propagator.inject(span.context(), headers, Map::put);
        return new MicrometerOutboxSendSpan(span, headers);
    }

    private record MicrometerOutboxSendSpan(Span span, Map<String, String> headers) implements OutboxSendSpan {

        @Override
        public void end(final Throwable error) {
            if (error != null) {
                this.span.error(error);
            }
            this.span.end();
        }
    }
}
