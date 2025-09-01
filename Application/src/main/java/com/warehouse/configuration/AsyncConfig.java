package com.warehouse.configuration;

import java.util.Map;

import org.slf4j.MDC;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

@Configuration
public class AsyncConfig {

    @Bean
    public TaskDecorator mdcTaskDecorator() {
        return runnable -> {
            Map<String, String> contextMap = MDC.getCopyOfContextMap();
            return () -> {
                Map<String, String> previous = MDC.getCopyOfContextMap();
                if (contextMap != null) MDC.setContextMap(contextMap); else MDC.clear();
                try { runnable.run(); }
                finally { if (previous != null) MDC.setContextMap(previous); else MDC.clear(); }
            };
        };
    }
}

