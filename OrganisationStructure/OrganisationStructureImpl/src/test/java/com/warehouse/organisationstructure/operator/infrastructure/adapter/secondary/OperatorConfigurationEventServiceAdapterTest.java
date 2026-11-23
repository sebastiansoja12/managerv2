package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.eventoutbox.application.port.secondary.IntegrationEventPublisher;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationEventServiceAdapterTest {

    @Mock
    private IntegrationEventPublisher integrationEventPublisher;

    @Test
    void shouldPublishOperatorCreatedIntegrationEventKeyedByOperator() {
        final OperatorConfigurationEventServiceAdapter adapter =
                new OperatorConfigurationEventServiceAdapter(integrationEventPublisher);
        final Instant timestamp = Instant.now();

        adapter.publishOperatorCreated(OperatorTestFixtures.operator().snapshot(), timestamp);

        final ArgumentCaptor<IntegrationEvent> eventCaptor = ArgumentCaptor.forClass(IntegrationEvent.class);
        verify(integrationEventPublisher).publish(eventCaptor.capture());
        final OperatorCreatedIntegrationEvent event = (OperatorCreatedIntegrationEvent) eventCaptor.getValue();
        assertEquals(OperatorTestFixtures.OPERATOR_ID, event.operatorId());
        assertEquals(OperatorTestFixtures.configurationDto(), event.configuration());
        assertEquals(timestamp, event.timestamp());
        assertEquals(String.valueOf(OperatorTestFixtures.OPERATOR_ID.getValue()), event.eventKey());
    }
}
