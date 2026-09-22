package com.warehouse.commonassets.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

import java.util.function.Supplier;

public class CircuitBreakerExecutor {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public CircuitBreakerExecutor(final CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    public <T> T execute(final String circuitBreakerName, final Supplier<T> operation) {
        final CircuitBreaker circuitBreaker = this.circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        return circuitBreaker.executeSupplier(operation);
    }

    public void execute(final String circuitBreakerName, final Runnable operation) {
        final CircuitBreaker circuitBreaker = this.circuitBreakerRegistry.circuitBreaker(circuitBreakerName);
        circuitBreaker.executeRunnable(operation);
    }
}
