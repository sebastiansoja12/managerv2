package com.warehouse.shipment;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.application.port.secondary.ShipmentReadModelRepository;
import com.warehouse.shipment.application.port.secondary.ShipmentRepository;
import com.warehouse.shipment.application.service.ShipmentReadModelSyncServiceImpl;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.domain.vo.ShipmentDeliveryDetails;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
                new DepartmentId(5L), null, null);
        when(shipmentRepository.findById(shipment.getShipmentId())).thenReturn(shipment);

        syncService.syncReadModel(shipment.getShipmentId(), deliveryDetails);

        verify(readModelRepository).sync(shipment.snapshot(), deliveryDetails);
    }
}
