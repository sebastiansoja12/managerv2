package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.commonassets.kafka.infrastructure.adapter.secondary.KafkaTemplateClient;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationKafkaServiceAdapterTest {

    @Mock
    private KafkaTemplateClient kafkaTemplateClient;

    @Test
    void shouldPublishOperatorCreatedIntegrationEvent() {
        final OperatorConfigurationKafkaServiceAdapter adapter =
                new OperatorConfigurationKafkaServiceAdapter(kafkaTemplateClient);
        final Instant timestamp = Instant.now();

        adapter.publishOperatorCreated(OperatorTestFixtures.operator().snapshot(), timestamp);

        final ArgumentCaptor<OperatorCreatedIntegrationEvent> eventCaptor =
                ArgumentCaptor.forClass(OperatorCreatedIntegrationEvent.class);
        verify(kafkaTemplateClient).publish(eq(String.valueOf(OperatorTestFixtures.OPERATOR_ID.getValue())),
                eventCaptor.capture());
        final OperatorCreatedIntegrationEvent event = eventCaptor.getValue();
        assertEquals(OperatorTestFixtures.OPERATOR_ID, event.operatorId());
        assertEquals(OperatorTestFixtures.configurationDto(), event.configuration());
        assertEquals(timestamp, event.timestamp());
    }
}
