package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import java.time.Instant;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

public class OperatorConfigurationKafkaServiceAdapter implements OperatorConfigurationEventServicePort {

    private final ApplicationEventPublisher eventPublisher;

    public OperatorConfigurationKafkaServiceAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
        eventPublisher.publishEvent(new OperatorCreatedIntegrationEvent(
                snapshot.operatorId(),
                OperatorMapper.toDtoConfiguration(snapshot.configuration()),
                timestamp
        ));
    }
}
