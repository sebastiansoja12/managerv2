package com.warehouse.create.infrastructure.adapter.primary;

import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.server.endpoint.mapping.PayloadRootAnnotationMethodEndpointMapping;

@Configuration
@EnableWs
public class DeliveryCreateAdapterConfig {


    public PayloadRootAnnotationMethodEndpointMapping payloadRootAnnotationMethodEndpointMapping() {
        return new PayloadRootAnnotationMethodEndpointMapping();
    }
}
