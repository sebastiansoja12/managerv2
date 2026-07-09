package com.warehouse.deliveryreturn.domain.vo;

import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;

public class UpdateStatusShipmentRequest {

    private final ProcessId processId;
    private final ShipmentId shipmentId;
    private final ShipmentStatus shipmentStatus = ShipmentStatus.RETURN;

    public UpdateStatusShipmentRequest(final ProcessId processId, final Long shipmentId) {
        this.processId = processId;
        this.shipmentId = new ShipmentId(shipmentId);
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }
}
