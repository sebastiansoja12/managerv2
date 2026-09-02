package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.kafka.infrastructure.adapter.primary.KafkaEventListener;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.department.api.dto.DepartmentCodeDto;
import com.warehouse.department.api.dto.DepartmentIdDto;
import com.warehouse.department.api.dto.DepartmentStatusDto;
import com.warehouse.department.api.event.DepartmentStatusChangedIntegrationEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DepartmentStatusChangedIntegrationEventListenerTest {

    @Mock
    private DeliveryNetworkPort deliveryNetworkPort;

    private DepartmentStatusChangedIntegrationEventListener listener;

    @BeforeEach
    void setUp() {
        this.listener = new DepartmentStatusChangedIntegrationEventListener(this.deliveryNetworkPort);
    }

    @Test
    void shouldRemoveConnectionsWhenDepartmentIsArchived() {
        this.listener.handle(event(DepartmentStatusDto.ARCHIVED));

        verify(this.deliveryNetworkPort).removeDepartmentConnections(new DepartmentId(10L));
    }

    @Test
    void shouldRemoveConnectionsWhenDepartmentIsDeleted() {
        this.listener.handle(event(DepartmentStatusDto.DELETED));

        verify(this.deliveryNetworkPort).removeDepartmentConnections(new DepartmentId(10L));
    }

    @Test
    void shouldIgnoreStatusThatStillParticipatesInNetwork() {
        this.listener.handle(event(DepartmentStatusDto.ACTIVE));

        verify(this.deliveryNetworkPort, never()).removeDepartmentConnections(any());
    }

    @Test
    void shouldConsumeDepartmentStatusEventsFromDedicatedKafkaTopic() throws NoSuchMethodException {
        final KafkaEventListener annotation = DepartmentStatusChangedIntegrationEventListener.class
                .getDeclaredMethod("handle", DepartmentStatusChangedIntegrationEvent.class)
                .getAnnotation(KafkaEventListener.class);

        assertNotNull(annotation);
        assertEquals(
                "${manager.kafka.topics.department-status-events:department.status.events}",
                annotation.topics()[0]);
        assertEquals(
                "${manager.kafka.consumer-groups.delivery-network-department-status:manager-delivery-network-department-status}",
                annotation.groupId());
    }

    private static DepartmentStatusChangedIntegrationEvent event(final DepartmentStatusDto status) {
        return new DepartmentStatusChangedIntegrationEvent(
                new DepartmentIdDto(10L),
                new DepartmentCodeDto("KT1"),
                status);
    }
}
