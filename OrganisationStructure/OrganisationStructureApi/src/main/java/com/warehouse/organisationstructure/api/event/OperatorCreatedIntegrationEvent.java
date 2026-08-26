package com.warehouse.organisationstructure.api.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.kafka.infrastructure.annotation.KafkaTopic;
import com.warehouse.organisationstructure.api.dto.OperatorConfigurationDto;

@KafkaTopic("${manager.kafka.topics.operator-configuration-create:operator.configuration.create}")
public record OperatorCreatedIntegrationEvent(
        OperatorId operatorId,
        OperatorConfigurationDto configuration,
        Instant timestamp
) {
}
