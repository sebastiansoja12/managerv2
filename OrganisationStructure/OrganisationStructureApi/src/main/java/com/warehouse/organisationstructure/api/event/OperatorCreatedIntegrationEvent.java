package com.warehouse.organisationstructure.api.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.domain.annotation.KafkaDomainEvent;
import com.warehouse.commonassets.kafka.domain.model.KafkaEventKey;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;

@KafkaDomainEvent(
        topicProperty = "manager.kafka.topics.operator-configuration-create",
        topic = "operator.configuration.create"
)
public record OperatorCreatedIntegrationEvent(
        OperatorId operatorId,
        OperatorConfigurationDto configuration,
        Instant timestamp
) implements KafkaEventKey {

    @Override
    public String kafkaKey() {
        return String.valueOf(operatorId.getValue());
    }
}
