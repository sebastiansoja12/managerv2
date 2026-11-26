package com.warehouse.eventoutbox.outbox.configuration;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

@ConfigurationProperties("manager.events.outbox")
public record OutboxProperties(@DefaultValue("false") boolean enabled,
                               @DefaultValue("true") boolean legacyEnvelope,
                               @DefaultValue("outbox.dead") String deadLetterTopic,
                               @DefaultValue Relay relay,
                               @DefaultValue Retry retry,
                               @DefaultValue Cleanup cleanup) {

    public record Relay(@DefaultValue("false") boolean enabled,
                        @DefaultValue("500ms") Duration pollInterval,
                        @DefaultValue("200") int batchSize,
                        @DefaultValue("10s") Duration sendTimeout,
                        @DefaultValue("7301202611") long lockKey) {

        public Relay {
            requirePositive(pollInterval, "manager.events.outbox.relay.poll-interval");
            requirePositive(sendTimeout, "manager.events.outbox.relay.send-timeout");
            requirePositive(batchSize, "manager.events.outbox.relay.batch-size");
        }
    }

    public record Retry(@DefaultValue("5s") Duration initialDelay,
                        @DefaultValue("5m") Duration maxDelay,
                        @DefaultValue("10") int maxAttempts) {
    }

    public record Cleanup(@DefaultValue("7d") Duration retention,
                          @DefaultValue("1h") Duration interval,
                          @DefaultValue("500") int batchSize) {

        public Cleanup {
            requirePositive(retention, "manager.events.outbox.cleanup.retention");
            requirePositive(interval, "manager.events.outbox.cleanup.interval");
            requirePositive(batchSize, "manager.events.outbox.cleanup.batch-size");
        }
    }

    private static void requirePositive(final Duration value, final String property) {
        if (value.isNegative() || value.isZero()) {
            throw new IllegalArgumentException(property + " must be positive");
        }
    }

    private static void requirePositive(final int value, final String property) {
        if (value <= 0) {
            throw new IllegalArgumentException(property + " must be positive");
        }
    }
}
