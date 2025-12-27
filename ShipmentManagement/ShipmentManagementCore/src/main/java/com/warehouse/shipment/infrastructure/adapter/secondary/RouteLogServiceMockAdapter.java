package com.warehouse.shipment.infrastructure.adapter.secondary;

import java.util.UUID;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.port.secondary.RouteLogServicePort;
import com.warehouse.shipment.domain.vo.*;

public class RouteLogServiceMockAdapter implements RouteLogServicePort {

    @Override
    public Result<RouteProcess, ErrorCode> notifyShipmentCreated(final ShipmentId shipmentId,
                                                                 final SoftwareConfiguration softwareConfiguration) {
        return Result.success(new RouteProcess(shipmentId, new ProcessId(UUID.randomUUID()), "", ""));
    }

    @Override
    public RouteProcess notifyRecipientChanged(final ShipmentId shipmentId, final Recipient recipient, final SoftwareConfiguration softwareConfiguration) {
        return new RouteProcess(shipmentId, new ProcessId(UUID.randomUUID()), "" , "");
    }

    @Override
    public RouteProcess notifyPersonChanged(final ShipmentId shipmentId, final Person person, final SoftwareConfiguration softwareConfiguration) {
        return null;
    }

    @Override
    public void notifyShipmentUpdated(final ShipmentSnapshot snapshot) {

    }
}
