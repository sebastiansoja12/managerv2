package com.warehouse.logistics.application.port.primary;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import com.warehouse.logistics.application.port.secondary.DeliveryCollectServicePort;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.SupplierServicePort;
import com.warehouse.logistics.domain.vo.SupplierAssignment;

class ShipmentCollectPortImplTest {

    private static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("5a0b7c1d-2e3f-4a5b-8c6d-7e8f9a0b1c2d"));
    private static final TrackingNumber COLLECTED = new TrackingNumber("TRK-001");
    private static final TrackingNumber FAILED = new TrackingNumber("TRK-002");
    private static final SupplierId SUPPLIER_ID = new SupplierId(4L);
    private static final VehicleId VEHICLE_ID = new VehicleId(9L);

    private final DeliveryCollectServicePort deliveryCollectServicePort = mock(DeliveryCollectServicePort.class);
    private final DeliveryServicePort deliveryServicePort = mock(DeliveryServicePort.class);
    private final SupplierServicePort supplierServicePort = mock(SupplierServicePort.class);

    private final ShipmentCollectPortImpl shipmentCollectPort = new ShipmentCollectPortImpl(
            deliveryCollectServicePort, deliveryServicePort, supplierServicePort);

    private final ShipmentCollectCommand command = new ShipmentCollectCommand(PROCESS_ID, List.of(COLLECTED, FAILED));

    private final ShipmentCollectResult collectResult = new ShipmentCollectResult(List.of(
            new ShipmentCollectDetail(COLLECTED, new ShipmentId(100L), true, null),
            new ShipmentCollectDetail(FAILED, null, false, "Shipment not found")));

    @Test
    void shouldAssignScanningSupplierAndVehicleToCollectedShipmentsOnly() {
        when(deliveryCollectServicePort.collect(command)).thenReturn(collectResult);
        when(supplierServicePort.findCurrentSupplier())
                .thenReturn(Optional.of(new SupplierAssignment(SUPPLIER_ID, VEHICLE_ID)));

        final ShipmentCollectResult result = shipmentCollectPort.collect(command);

        assertSame(collectResult, result);
        verify(deliveryServicePort).assignShipmentSupplier(new ShipmentId(100L), SUPPLIER_ID, VEHICLE_ID);
        verify(deliveryServicePort, never()).assignShipmentSupplier(null, SUPPLIER_ID, VEHICLE_ID);
    }

    @Test
    void shouldNotAssignSupplierWhenScanningUserIsNotSupplier() {
        when(deliveryCollectServicePort.collect(command)).thenReturn(collectResult);
        when(supplierServicePort.findCurrentSupplier()).thenReturn(Optional.empty());

        shipmentCollectPort.collect(command);

        verify(deliveryServicePort, never()).assignShipmentSupplier(any(), any(), any());
    }

    @Test
    void shouldKeepCollectResultWhenSupplierAssignmentFails() {
        when(deliveryCollectServicePort.collect(command)).thenReturn(collectResult);
        when(supplierServicePort.findCurrentSupplier())
                .thenReturn(Optional.of(new SupplierAssignment(SUPPLIER_ID, VEHICLE_ID)));
        doThrow(new IllegalStateException("Delivery cannot be modified"))
                .when(deliveryServicePort).assignShipmentSupplier(new ShipmentId(100L), SUPPLIER_ID, VEHICLE_ID);

        assertSame(collectResult, shipmentCollectPort.collect(command));
    }
}
