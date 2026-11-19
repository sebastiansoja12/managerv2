package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.service.ShipmentReadModelSyncServiceImpl;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ShipmentReadModelSyncServiceImplTest {

    private final ShipmentReadModelRepository readModelRepository = mock(ShipmentReadModelRepository.class);
    private final ShipmentRepository shipmentRepository = mock(ShipmentRepository.class);
    private final ShipmentReadModelSyncServiceImpl syncService =
            new ShipmentReadModelSyncServiceImpl(readModelRepository, shipmentRepository);

    @Test
    void shouldRebuildReadModelFromCurrentShipmentStateWithDeliveryDetails() {
        final Shipment shipment = DataTestCreator.shipment();
        final ShipmentDeliveryDetails deliveryDetails = new ShipmentDeliveryDetails(new DepartmentId(3L),
                new DepartmentId(5L), new DepartmentId(3L), null, null);
        when(shipmentRepository.findById(shipment.getShipmentId())).thenReturn(shipment);

        syncService.syncReadModel(shipment.getShipmentId(), deliveryDetails);

        verify(readModelRepository).sync(shipment.snapshot(), deliveryDetails);
    }

    @Test
    void shouldSyncShipmentStateAndKeepDeliveryDetailsFromDeliveryEvents() {
        final Shipment shipment = DataTestCreator.shipment();
        when(shipmentRepository.findById(shipment.getShipmentId())).thenReturn(shipment);

        syncService.syncReadModel(shipment.getShipmentId());

        verify(readModelRepository).sync(shipment.snapshot());
        verify(readModelRepository, never()).sync(any(), any());
    }

    @Test
    void shouldRebuildReadModelsForDateRangeWithoutQueryingDeliveries() {
        final Shipment shipment = DataTestCreator.shipment();
        final LocalDate dateFrom = LocalDate.of(2026, 11, 1);
        final LocalDate dateTo = LocalDate.of(2026, 11, 5);
        when(shipmentRepository.findAllCreatedBetween(dateFrom.atStartOfDay(), dateTo.plusDays(1).atStartOfDay()))
                .thenReturn(List.of(shipment));

        final int synced = syncService.syncReadModels(dateFrom, dateTo);

        assertEquals(1, synced);
        verify(readModelRepository).sync(shipment.snapshot());
        verify(readModelRepository, never()).sync(any(), any());
    }

    @Test
    void shouldSyncPalletTrackingNumberWithoutRebuildingShipmentState() {
        final ShipmentId shipmentId = new ShipmentId(7L);
        final PalletTrackingNumber palletTrackingNumber = new PalletTrackingNumber("PAL000000000001");

        syncService.syncPalletTrackingNumber(shipmentId, palletTrackingNumber);

        verify(readModelRepository).changePalletTrackingNumber(shipmentId, palletTrackingNumber);
        verify(readModelRepository, never()).sync(any());
        verifyNoInteractions(shipmentRepository);
    }

    @Test
    void shouldClearPalletTrackingNumberWhenShipmentLeavesPallet() {
        final ShipmentId shipmentId = new ShipmentId(7L);

        syncService.syncPalletTrackingNumber(shipmentId, null);

        verify(readModelRepository).changePalletTrackingNumber(shipmentId, null);
    }
}
