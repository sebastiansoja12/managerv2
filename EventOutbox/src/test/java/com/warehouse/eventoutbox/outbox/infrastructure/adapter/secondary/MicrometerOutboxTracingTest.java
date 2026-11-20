package com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary;

import static com.warehouse.eventoutbox.outbox.OutboxFixture.NOW;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import com.warehouse.eventoutbox.integration.model.EventId;
import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.domain.enumeration.OutboxEventStatus;
import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.otel.bridge.OtelBaggageManager;
import io.micrometer.tracing.otel.bridge.OtelCurrentTraceContext;
import io.micrometer.tracing.otel.bridge.OtelPropagator;
import io.micrometer.tracing.otel.bridge.OtelTracer;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

class MicrometerOutboxTracingTest {

    private static final String TRACE_ID = "0af7651916cd43dd8448eb211c80319c";
    private static final String PARENT_SPAN_ID = "b7ad6b7169203331";
    private static final String STORED_TRACEPARENT = "00-" + TRACE_ID + "-" + PARENT_SPAN_ID + "-01";

    private final List<SpanData> exported = new CopyOnWriteArrayList<>();
    private final SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
            .addSpanProcessor(SimpleSpanProcessor.create(new CollectingExporter(this.exported)))
            .build();
    private final io.opentelemetry.api.trace.Tracer otelTracer = this.tracerProvider.get("test");
    private final OtelCurrentTraceContext currentTraceContext = new OtelCurrentTraceContext();
    private final Tracer tracer = new OtelTracer(this.otelTracer, this.currentTraceContext, event -> {
    }, new OtelBaggageManager(this.currentTraceContext, List.of(), List.of()));
    private final OtelPropagator propagator = new OtelPropagator(
            ContextPropagators.create(W3CTraceContextPropagator.getInstance()), this.otelTracer);
    private final OutboxTracing tracing = new MicrometerOutboxTracing(this.tracer, this.propagator);

    @AfterEach
    void tearDown() {
        this.tracerProvider.close();
    }

    @Test
    void shouldCaptureTraceContextOfCurrentSpan() {
        final Span request = this.tracer.nextSpan().name("request").start();
        try (Tracer.SpanInScope ignored = this.tracer.withSpan(request)) {
            assertThat(this.tracing.currentContext().get("traceparent"))
                    .startsWith("00-" + request.context().traceId() + "-" + request.context().spanId() + "-");
        } finally {
            request.end();
        }
    }

    @Test
    void shouldCaptureNothingWithoutCurrentSpan() {
        assertThat(this.tracing.currentContext()).isEmpty();
    }

    @Test
    void shouldContinueStoredTraceInProducerSpan() {
        final OutboxEvent event = event(Map.of("ce_type", "shipment.created", "traceparent", STORED_TRACEPARENT));

        final OutboxTracing.OutboxSendSpan span = this.tracing.startSend(event);
        span.end(null);

        assertThat(this.exported).singleElement().satisfies(spanData -> {
            assertThat(spanData.getName()).isEqualTo("outbox publish");
            assertThat(spanData.getKind()).isEqualTo(SpanKind.PRODUCER);
            assertThat(spanData.getTraceId()).isEqualTo(TRACE_ID);
            assertThat(spanData.getParentSpanId()).isEqualTo(PARENT_SPAN_ID);
            assertThat(spanData.getAttributes().asMap().values()).contains("shipment.events", "shipment.created");
            assertThat(span.headers())
                    .containsEntry("ce_type", "shipment.created")
                    .hasEntrySatisfying("traceparent", traceparent -> assertThat(traceparent)
                            .startsWith("00-" + TRACE_ID + "-" + spanData.getSpanId() + "-"));
        });
    }

    @Test
    void shouldStartNewTraceWhenEventHasNoStoredContext() {
        final OutboxTracing.OutboxSendSpan span = this.tracing.startSend(event(Map.of("ce_type", "shipment.created")));
        span.end(null);

        assertThat(this.exported).singleElement().satisfies(spanData -> {
            assertThat(spanData.getParentSpanContext().isValid()).isFalse();
            assertThat(span.headers()).containsKey("traceparent");
        });
    }

    @Test
    void shouldMarkSpanAsFailedWhenSendFails() {
        final OutboxTracing.OutboxSendSpan span = this.tracing.startSend(event(Map.of("traceparent", STORED_TRACEPARENT)));

        span.end(new IllegalStateException("broker down"));

        assertThat(this.exported).singleElement()
                .satisfies(spanData -> assertThat(spanData.getStatus().getStatusCode()).isEqualTo(StatusCode.ERROR));
    }

    private OutboxEvent event(final Map<String, String> headers) {
        return new OutboxEvent(EventId.nextId(), "shipment.created", 1, "shipment.events", "SH-1", "{}", headers,
                NOW, OutboxEventStatus.PENDING, 0, NOW, null, null);
    }

    private record CollectingExporter(List<SpanData> spans) implements SpanExporter {

        @Override
        public CompletableResultCode export(final Collection<SpanData> spans) {
            this.spans.addAll(spans);
            return CompletableResultCode.ofSuccess();
        }

        @Override
        public CompletableResultCode flush() {
            return CompletableResultCode.ofSuccess();
        }

        @Override
        public CompletableResultCode shutdown() {
            return CompletableResultCode.ofSuccess();
        }
    }
}
