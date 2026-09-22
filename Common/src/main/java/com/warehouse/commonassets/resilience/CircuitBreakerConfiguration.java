package com.warehouse.commonassets.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        return CircuitBreakerRegistry.ofDefaults();
    }

    @Bean
    @ConditionalOnMissingBean
    public CircuitBreakerExecutor circuitBreakerExecutor(final CircuitBreakerRegistry circuitBreakerRegistry) {
        return new CircuitBreakerExecutor(circuitBreakerRegistry);
    }
}
