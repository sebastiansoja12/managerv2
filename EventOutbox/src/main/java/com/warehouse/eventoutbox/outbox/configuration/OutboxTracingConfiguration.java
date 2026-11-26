package com.warehouse.eventoutbox.outbox.configuration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.eventoutbox.outbox.application.port.secondary.OutboxTracing;
import com.warehouse.eventoutbox.outbox.infrastructure.adapter.secondary.MicrometerOutboxTracing;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.propagation.Propagator;

@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(name = "io.micrometer.tracing.Tracer")
public class OutboxTracingConfiguration {

    @Bean
    public OutboxTracing outboxTracing(final ObjectProvider<Tracer> tracer, final ObjectProvider<Propagator> propagator) {
        final Tracer availableTracer = tracer.getIfAvailable();
        final Propagator availablePropagator = propagator.getIfAvailable();
        return availableTracer == null || availablePropagator == null
                ? OutboxTracing.NOOP
                : new MicrometerOutboxTracing(availableTracer, availablePropagator);
    }
}
