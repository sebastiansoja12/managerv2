package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.api.event.OperatorCreatedIntegrationEvent;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;

@ExtendWith(MockitoExtension.class)
class OperatorConfigurationKafkaEventListenerTest {

    @Mock
    private OperatorConfigurationPort operatorConfigurationPort;

    @Test
    void shouldCreateConfigurationFromOperatorCreatedEvent() {
        final OperatorConfigurationKafkaEventListener listener =
                new OperatorConfigurationKafkaEventListener(operatorConfigurationPort);
        final OperatorCreatedIntegrationEvent event = new OperatorCreatedIntegrationEvent(
                OperatorTestFixtures.OPERATOR_ID,
                OperatorTestFixtures.configurationDto(),
                Instant.now()
        );

        listener.handle(event);

        final ArgumentCaptor<OperatorConfiguration> configurationCaptor =
                ArgumentCaptor.forClass(OperatorConfiguration.class);
        verify(operatorConfigurationPort).create(
                eq(OperatorTestFixtures.OPERATOR_ID),
                configurationCaptor.capture()
        );
        final OperatorConfiguration configuration = configurationCaptor.getValue();
        assertEquals(31.5, configuration.getShipmentLimits().getMaxWeight());
        assertEquals(4, configuration.getDeliveryTimeConfiguration().getMaxDeliveryDays());
        assertTrue(configuration.getShippingCapabilities().isSupportsInternationalShipping());
    }
}
