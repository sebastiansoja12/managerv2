package com.warehouse.commonassets.event.integration.context;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;

import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.eventoutbox.integration.context.IntegrationEventContext;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.kafka.KafkaEventHeaders;

import tools.jackson.databind.node.ObjectNode;

@Component
public class OperatorIntegrationEventContext implements IntegrationEventContext {

    private final ObjectProvider<OperatorContextProvider> operatorContextProvider;

    public OperatorIntegrationEventContext(final ObjectProvider<OperatorContextProvider> operatorContextProvider) {
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public Map<String, String> attributes(final IntegrationEvent event) {
        final Map<String, String> attributes = new LinkedHashMap<>();
        currentContext().ifPresent(context -> {
            if (context.operatorId() != null) {
                attributes.put(KafkaEventHeaders.OPERATOR_ID, String.valueOf(context.operatorId().getValue()));
            }
            if (context.userId() != null) {
                attributes.put(KafkaEventHeaders.USER_ID, String.valueOf(context.userId().getValue()));
            }
            if (context.departmentId() != null) {
                attributes.put(KafkaEventHeaders.DEPARTMENT_ID, String.valueOf(context.departmentId().getValue()));
            }
        });
        return attributes;
    }

    @Override
    public void enrichLegacyPayload(final ObjectNode payload, final Map<String, String> attributes) {
        putIdentifier(payload, KafkaEventHeaders.OPERATOR_ID, attributes.get(KafkaEventHeaders.OPERATOR_ID));
        putIdentifier(payload, KafkaEventHeaders.USER_ID, attributes.get(KafkaEventHeaders.USER_ID));
        putIdentifier(payload, KafkaEventHeaders.DEPARTMENT_ID, attributes.get(KafkaEventHeaders.DEPARTMENT_ID));
    }

    private Optional<OperatorDetails> currentContext() {
        final OperatorContextProvider provider = this.operatorContextProvider.getIfAvailable();
        return provider == null ? Optional.empty() : provider.currentContext();
    }

    private static void putIdentifier(final ObjectNode payload, final String name, final String value) {
        if (value == null && payload.has(name)) {
            return;
        }
        if (value == null) {
            payload.putNull(name);
            return;
        }
        payload.putObject(name).put("value", Long.parseLong(value));
    }
}
