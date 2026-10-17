package com.warehouse.shipment.application.port.primary.result;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;
import com.warehouse.shipment.domain.model.Signature;

public record ShipmentResult(ShipmentSnapshot snapshot,
                             DepartmentCode destination,
                             Signature signature) {

    public ShipmentResult(final ShipmentSnapshot snapshot, final DepartmentCode destination) {
        this(snapshot, destination, null);
    }
}
