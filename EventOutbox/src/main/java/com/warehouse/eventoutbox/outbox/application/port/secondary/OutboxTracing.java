package com.warehouse.eventoutbox.outbox.application.port.secondary;

import java.util.Map;

import com.warehouse.eventoutbox.outbox.domain.model.OutboxEvent;

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

    Map<String, String> currentContext();

    OutboxSendSpan startSend(OutboxEvent event);

    interface OutboxSendSpan {

        Map<String, String> headers();

        void end(Throwable error);
    }
}
