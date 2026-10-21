package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryHistoryRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryHistoryEventType;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.Optional;

import static com.warehouse.logisticsdelivery.DeliveryFixture.DESTINATION_DEPARTMENT_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.USER_ID;
import static com.warehouse.logisticsdelivery.DeliveryFixture.shipmentDelivery;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DeliveryHistoryPortImplTest {

    private DeliveryHistoryRepository deliveryHistoryRepository;
    private OperatorContextProvider operatorContextProvider;
    private DeliveryHistoryPortImpl deliveryHistoryPort;

    @BeforeEach
    void setUp() {
        this.deliveryHistoryRepository = mock(DeliveryHistoryRepository.class);
        this.operatorContextProvider = mock(OperatorContextProvider.class);
        this.deliveryHistoryPort = new DeliveryHistoryPortImpl(this.deliveryHistoryRepository,
                this.operatorContextProvider);
    }

    @Test
    void shouldRecordModifiedSnapshotWithCurrentUser() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        final Instant occurredAt = Instant.parse("2026-10-21T08:00:00Z");
        when(this.operatorContextProvider.currentContext())
                .thenReturn(Optional.of(new OperatorDetails(OperatorId.of(7L), USER_ID, new DepartmentId(3L))));

        this.deliveryHistoryPort.record(new DeliveryModified(delivery.snapshot(), occurredAt));

        final ArgumentCaptor<DeliveryHistoryEntry> entry = ArgumentCaptor.forClass(DeliveryHistoryEntry.class);
        verify(this.deliveryHistoryRepository).save(entry.capture());
        assertThat(entry.getValue().eventType()).isEqualTo(DeliveryHistoryEventType.MODIFIED);
        assertThat(entry.getValue().occurredAt()).isEqualTo(occurredAt);
        assertThat(entry.getValue().userId()).isEqualTo(USER_ID);
        assertThat(entry.getValue().destinationDepartmentId()).isEqualTo(DESTINATION_DEPARTMENT_ID);
        assertThat(entry.getValue().stepCount()).isEqualTo(1);
    }

    @Test
    void shouldRecordAttemptWithoutOperatorContext() {
        final Delivery delivery = shipmentDelivery(new ShipmentId(10L));
        when(this.operatorContextProvider.currentContext()).thenReturn(Optional.empty());

        this.deliveryHistoryPort.record(new DeliveryAttemptRecorded(delivery.snapshot(), Instant.now()));

        final ArgumentCaptor<DeliveryHistoryEntry> entry = ArgumentCaptor.forClass(DeliveryHistoryEntry.class);
        verify(this.deliveryHistoryRepository).save(entry.capture());
        assertThat(entry.getValue().eventType()).isEqualTo(DeliveryHistoryEventType.ATTEMPT_RECORDED);
        assertThat(entry.getValue().userId()).isNull();
    }
}
