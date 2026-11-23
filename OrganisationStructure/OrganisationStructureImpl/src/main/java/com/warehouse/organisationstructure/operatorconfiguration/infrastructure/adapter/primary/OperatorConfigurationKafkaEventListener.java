package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import org.springframework.stereotype.Component;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventListener;
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

    @IntegrationEventListener(group = "manager-operator-configuration-create")
    public void handle(final OperatorCreatedIntegrationEvent event) {
        final OperatorConfiguration configuration = OperatorConfigurationEventMapper.toModel(event.configuration());
        operatorConfigurationPort.create(event.operatorId(), configuration);
    }
}
