package com.warehouse.logistics.infrastructure.adapter.primary;

import org.springframework.core.Ordered;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.server.EndpointExceptionResolver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoggingSoapEndpointExceptionResolver implements EndpointExceptionResolver, Ordered {

    @Override
    public boolean resolveException(final MessageContext messageContext,
                                    final Object endpoint,
                                    final Exception ex) {
        log.error("Unhandled SOAP exception for endpoint {}: {}", resolveEndpointName(endpoint), ex.getMessage(), ex);
        return false;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String resolveEndpointName(final Object endpoint) {
        return endpoint != null ? endpoint.toString() : "unknown";
    }
}
