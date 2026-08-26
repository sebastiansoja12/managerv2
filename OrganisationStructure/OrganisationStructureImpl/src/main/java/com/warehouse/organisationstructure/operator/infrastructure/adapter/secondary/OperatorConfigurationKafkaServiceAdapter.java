package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import java.time.Instant;

import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

public class OperatorConfigurationKafkaServiceAdapter implements OperatorConfigurationEventServicePort {

    private final KafkaTemplateClient kafkaTemplateClient;

    public OperatorConfigurationKafkaServiceAdapter(final KafkaTemplateClient kafkaTemplateClient) {
        this.kafkaTemplateClient = kafkaTemplateClient;
    }

    @Override
    public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
        final OperatorCreatedIntegrationEvent event = new OperatorCreatedIntegrationEvent(
                snapshot.operatorId(),
                OperatorMapper.toDtoConfiguration(snapshot.configuration()),
                timestamp
        );
        this.kafkaTemplateClient.publish(String.valueOf(snapshot.operatorId().getValue()), event);
    }
}
