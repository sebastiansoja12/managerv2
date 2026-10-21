package com.warehouse.palletmanagement.application.port.secondary;

import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.vo.PalletShipment;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;

import java.util.Optional;

public interface ShipmentServicePort {
    ShipmentLoadResult loadOnPallet(final ShipmentId shipmentId);
    Optional<PalletShipment> findShipment(final ShipmentId shipmentId);
    Optional<PalletDocumentShipment> describeShipment(final ShipmentId shipmentId);
}
