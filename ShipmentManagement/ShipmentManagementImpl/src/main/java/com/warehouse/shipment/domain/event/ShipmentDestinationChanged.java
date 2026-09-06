package com.warehouse.shipment.domain.event;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

import java.time.Instant;

public class ShipmentDestinationChanged extends ShipmentStatusChanged implements ShipmentEvent {

    private final DepartmentId destinationDepartmentId;

    public ShipmentDestinationChanged(final ShipmentSnapshot snapshot, final DepartmentId destinationDepartmentId,
                                      final Instant timestamp) {
        super(snapshot, timestamp);
        this.destinationDepartmentId = destinationDepartmentId;
    }

    public DepartmentId getDestinationDepartmentId() {
        return destinationDepartmentId;
    }
}
