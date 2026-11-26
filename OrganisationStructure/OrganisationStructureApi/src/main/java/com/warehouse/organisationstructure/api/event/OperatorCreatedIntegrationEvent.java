package com.warehouse.organisationstructure.api.event;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;

@JsonIgnoreProperties(ignoreUnknown = true)
@IntegrationEventType(value = "operator.created", version = 1, topic = OperatorTopics.CONFIGURATION_CREATE)
@KafkaTopic("${manager.kafka.topics.operator-configuration-create:operator.configuration.create}")
public record OperatorCreatedIntegrationEvent(
        OperatorId operatorId,
        OperatorConfigurationDto configuration,
        Instant timestamp
) implements IntegrationEvent, IntegrationEventKey {

    @Override
    public String eventKey() {
        return String.valueOf(this.operatorId.getValue());
    }
}
