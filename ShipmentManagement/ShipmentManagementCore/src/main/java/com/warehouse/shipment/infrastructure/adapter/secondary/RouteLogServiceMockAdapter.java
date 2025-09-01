package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.Person;
import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.RouteProcess;
import com.warehouse.shipment.domain.vo.SoftwareConfiguration;

import java.util.UUID;

public class RouteLogServiceMockAdapter implements RouteLogServicePort {

    @Override
    public RouteProcess notifyShipmentCreated(final ShipmentId shipmentId,
                                              final SoftwareConfiguration softwareConfiguration) {
        return new RouteProcess(shipmentId, new ProcessId(UUID.randomUUID()), "", "");
    }

    @Override
    public RouteProcess notifyRecipientChanged(final ShipmentId shipmentId, final Recipient recipient, final SoftwareConfiguration softwareConfiguration) {
        return new RouteProcess(shipmentId, new ProcessId(UUID.randomUUID()), "" , "");
    }

    @Override
    public RouteProcess notifyPersonChanged(final ShipmentId shipmentId, final Person person, final SoftwareConfiguration softwareConfiguration) {
        return null;
    }
}
