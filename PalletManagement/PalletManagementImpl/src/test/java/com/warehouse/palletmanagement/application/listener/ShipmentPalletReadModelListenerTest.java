package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.PalletFixture;
import com.warehouse.palletmanagement.application.port.secondary.PalletIntegrationEventServicePort;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;
import com.warehouse.shipment.api.event.ShipmentPalletReadModelChanged;
import com.warehouse.shipment.api.event.snapshot.ShipmentPalletReadModelData;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ShipmentPalletReadModelListenerTest {

    private final PalletIntegrationEventServicePort servicePort = mock(PalletIntegrationEventServicePort.class);
    private final ShipmentPalletReadModelListener listener = new ShipmentPalletReadModelListener(servicePort);

    @Test
    void shouldPublishPalletTrackingNumberForEveryLoadedShipment() {
        listener.handle(new PalletLoaded(new PalletLoadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L), new ShipmentId(2L)),
                new DepartmentId(5L), new DepartmentId(6L)), Instant.now()));

        final ArgumentCaptor<ShipmentPalletReadModelChanged> event =
                ArgumentCaptor.forClass(ShipmentPalletReadModelChanged.class);
        verify(servicePort, times(2)).publishEvent(event.capture());
        assertThat(event.getAllValues()).extracting(ShipmentPalletReadModelChanged::payload).containsExactly(
                new ShipmentPalletReadModelData(new ShipmentId(1L), PalletFixture.TRACKING_NUMBER),
                new ShipmentPalletReadModelData(new ShipmentId(2L), PalletFixture.TRACKING_NUMBER));
        assertThat(event.getAllValues()).extracting(ShipmentPalletReadModelChanged::eventKey)
                .containsExactly("1", "2");
    }

    @Test
    void shouldClearPalletTrackingNumberWhenLoadingIsReverted() {
        listener.handle(new PalletLoadingReverted(new PalletLoadingRevertedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L))), Instant.now()));

        final ArgumentCaptor<ShipmentPalletReadModelChanged> event =
                ArgumentCaptor.forClass(ShipmentPalletReadModelChanged.class);
        verify(servicePort).publishEvent(event.capture());
        assertThat(event.getValue().payload()).isEqualTo(new ShipmentPalletReadModelData(new ShipmentId(1L), null));
    }

    @Test
    void shouldClearPalletTrackingNumberOnlyForUnloadedShipments() {
        listener.handle(new PalletUnloaded(new PalletUnloadedSnapshot(PalletFixture.PALLET_ID,
                PalletFixture.TRACKING_NUMBER, List.of(new ShipmentId(1L)), List.of(new ShipmentId(2L)),
                new DepartmentId(6L)), Instant.now()));

        final ArgumentCaptor<ShipmentPalletReadModelChanged> event =
                ArgumentCaptor.forClass(ShipmentPalletReadModelChanged.class);
        verify(servicePort).publishEvent(event.capture());
        assertThat(event.getValue().payload()).isEqualTo(new ShipmentPalletReadModelData(new ShipmentId(1L), null));
    }
}
