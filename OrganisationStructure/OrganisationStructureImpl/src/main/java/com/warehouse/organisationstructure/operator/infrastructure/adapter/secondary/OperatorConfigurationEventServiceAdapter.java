package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import java.time.Instant;

import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

public class OperatorConfigurationEventServiceAdapter implements OperatorConfigurationEventServicePort {

    private final IntegrationEventPublisher integrationEventPublisher;

    public OperatorConfigurationEventServiceAdapter(final IntegrationEventPublisher integrationEventPublisher) {
        this.integrationEventPublisher = integrationEventPublisher;
    }

    @Override
    public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
        this.integrationEventPublisher.publish(new OperatorCreatedIntegrationEvent(
                snapshot.operatorId(),
                OperatorMapper.toDtoConfiguration(snapshot.configuration()),
                timestamp
        ));
    }
}
