package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.infrastructure.event.OperatorGeocodingConfigurationCreateEvent;

@ExtendWith(MockitoExtension.class)
class OperatorGeocodingConfigurationEventServiceAdapterTest {

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Test
    void shouldPublishGeocodingConfigurationCreateEvent() {
        final OperatorGeocodingConfigurationEventServiceAdapter adapter =
                new OperatorGeocodingConfigurationEventServiceAdapter(eventPublisher);
        final Instant timestamp = Instant.now();

        adapter.publishOperatorCreated(OperatorTestFixtures.operator().snapshot(), timestamp);

        final ArgumentCaptor<OperatorGeocodingConfigurationCreateEvent> eventCaptor =
                ArgumentCaptor.forClass(OperatorGeocodingConfigurationCreateEvent.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        final OperatorGeocodingConfigurationCreateEvent event = eventCaptor.getValue();
        assertEquals(OperatorTestFixtures.OPERATOR_ID, event.operatorId());
        assertEquals("position-stack-api-key", event.configuration().apiKey());
        assertNull(event.configuration().apiPassword());
        assertEquals(timestamp, event.timestamp());
    }
}
