package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.mapper.OperatorConfigurationEventMapper;

@Component
public class OperatorConfigurationKafkaEventListener {

    private final OperatorConfigurationPort operatorConfigurationPort;

    public OperatorConfigurationKafkaEventListener(final OperatorConfigurationPort operatorConfigurationPort) {
        this.operatorConfigurationPort = operatorConfigurationPort;
    }

    @KafkaEventListener(
            topics = "${manager.kafka.topics.operator-configuration-create:operator.configuration.create}",
            groupId = "${manager.kafka.consumer-groups.operator-configuration-create:manager-operator-configuration-create}"
    )
    public void handle(final OperatorCreatedIntegrationEvent event) {
        final OperatorConfiguration configuration = OperatorConfigurationEventMapper.toModel(event.configuration());
        operatorConfigurationPort.create(event.operatorId(), configuration);
    }
}
