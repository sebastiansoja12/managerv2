package com.warehouse.returning.api;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import java.util.Optional;

public interface ReturningApiService {
    Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId);
}
