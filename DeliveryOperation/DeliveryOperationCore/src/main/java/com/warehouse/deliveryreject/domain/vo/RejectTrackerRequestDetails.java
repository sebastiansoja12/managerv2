package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public class RejectTrackerRequestDetails {
    private final ShipmentId shipmentId;
    private final ShipmentId newShipmentId;
    private final RejectReason rejectReason;
    private final ShipmentStatus shipmentStatus;
    private final ProcessType processType;

    public RejectTrackerRequestDetails(final ShipmentId shipmentId,
                                       final ShipmentId newShipmentId,
                                       final RejectReason rejectReason,
                                       final ShipmentStatus shipmentStatus,
                                       final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.rejectReason = rejectReason;
        this.shipmentStatus = shipmentStatus;
        this.processType = processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public RejectReason getRejectReason() {
        return rejectReason;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public ProcessType getProcessType() {
        return processType;
    }
}
