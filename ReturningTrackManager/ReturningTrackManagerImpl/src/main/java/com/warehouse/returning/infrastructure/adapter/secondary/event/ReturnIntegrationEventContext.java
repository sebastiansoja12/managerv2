package com.warehouse.returning.infrastructure.adapter.secondary.event;

import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;
import com.warehouse.returning.api.event.ReturnLifecycleIntegrationEvent;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * RTM has no operator security context of the manager application; every return event carries its operator scope
 * itself, so the headers are taken from the event.
 */
@Component
public class ReturnIntegrationEventContext implements IntegrationEventContext {

    @Override
    public Map<String, String> attributes(final IntegrationEvent event) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        if (event instanceof final ReturnLifecycleIntegrationEvent returnEvent) {
            attributes.put(KafkaEventHeaders.OPERATOR_ID, String.valueOf(returnEvent.operatorId().value()));
            attributes.put(KafkaEventHeaders.DEPARTMENT_ID, String.valueOf(returnEvent.departmentId().value()));
            if (returnEvent.userId() != null) {
                attributes.put(KafkaEventHeaders.USER_ID, String.valueOf(returnEvent.userId().value()));
            }
        }
        return attributes;
    }
}
