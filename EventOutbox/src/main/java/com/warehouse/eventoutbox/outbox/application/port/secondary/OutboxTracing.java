package com.warehouse.eventoutbox.outbox.application.port.secondary;

import java.util.Map;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

/**
 * Carries the trace across the outbox: the context of the business transaction is stored with the event and the
 * relay, which sends from another thread, continues it as the parent of the Kafka producer span.
 */
public interface OutboxTracing {

    OutboxTracing NOOP = new OutboxTracing() {

        @Override
        public Map<String, String> currentContext() {
            return Map.of();
        }

        @Override
        public OutboxSendSpan startSend(final OutboxEvent event) {
            return new OutboxSendSpan() {

                @Override
                public Map<String, String> headers() {
                    return event.getHeaders();
                }

                @Override
                public void end(final Throwable error) {
                }
            };
        }
    };

    /**
     * Propagation headers (W3C {@code traceparent}, {@code tracestate}) of the span active in the calling thread,
     * empty when there is none.
     */
    Map<String, String> currentContext();

    /**
     * Starts the producer span of the given event, child of the context stored with it.
     */
    OutboxSendSpan startSend(OutboxEvent event);

    interface OutboxSendSpan {

        /**
         * Headers to send: the stored ones with the propagation headers pointing at this span.
         */
        Map<String, String> headers();

        void end(Throwable error);
    }
}
