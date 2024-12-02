package com.warehouse.deliveryreject.domain.model;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.ShipmentId;

public class RejectTrackerRequest {
    private ShipmentId shipmentId;
    private RejectReason rejectReason;
    private ShipmentStatus shipmentStatus;
    private ProcessType processType;

    public RejectTrackerRequest(final ShipmentId shipmentId,
                                final RejectReason rejectReason,
                                final ShipmentStatus shipmentStatus,
                                final ProcessType processType) {
        this.shipmentId = shipmentId;
        this.rejectReason = rejectReason;
        this.shipmentStatus = shipmentStatus;
        this.processType = processType;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
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
