package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import java.util.Optional;

public interface ReturningServicePort {
    Optional<ReturnDetailsDto> findReturnByShipmentId(final ShipmentId shipmentId);
}
