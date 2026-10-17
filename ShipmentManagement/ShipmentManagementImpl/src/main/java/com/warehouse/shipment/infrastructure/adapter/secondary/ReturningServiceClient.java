package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.shipment.application.port.secondary.ReturningServicePort;
import java.util.Optional;

public class ReturningServiceClient implements ReturningServicePort {
    private final ReturningApiService returningApiService;

    public ReturningServiceClient(final ReturningApiService returningApiService) {
        this.returningApiService = returningApiService;
    }

    @Override
    public Optional<ReturnDetailsDto> findReturnByShipmentId(final ShipmentId shipmentId) {
        return returningApiService.findByShipmentId(shipmentId);
    }
}
