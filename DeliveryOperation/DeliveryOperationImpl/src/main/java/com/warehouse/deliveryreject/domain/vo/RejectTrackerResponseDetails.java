package com.warehouse.deliveryreject.domain.vo;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;

public class RejectTrackerResponseDetails {
    private final ShipmentId shipmentId;
    private final ShipmentId newShipmentId;
    private final Boolean loggedInTracker;
    private final ProcessId processId;

    public RejectTrackerResponseDetails(final ShipmentId shipmentId,
                                        final ShipmentId newShipmentId,
                                        final Boolean loggedInTracker,
                                        final ProcessId processId) {
        this.shipmentId = shipmentId;
        this.newShipmentId = newShipmentId;
        this.loggedInTracker = loggedInTracker;
        this.processId = processId;
    }

    public ShipmentId getShipmentId() {
        return shipmentId;
    }

    public ShipmentId getNewShipmentId() {
        return newShipmentId;
    }

    public Boolean getLoggedInTracker() {
        return loggedInTracker;
    }

    public ProcessId getProcessId() {
        return processId;
    }
}
