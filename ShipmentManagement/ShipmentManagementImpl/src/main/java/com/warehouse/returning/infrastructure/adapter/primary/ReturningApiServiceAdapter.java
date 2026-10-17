package com.warehouse.returning.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returning.api.ReturningApiService;
import com.warehouse.returning.api.dto.ReturnDetailsDto;
import com.warehouse.returning.application.port.primary.ReturnQueryPort;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ReturningApiServiceAdapter implements ReturningApiService {
    private final ReturnQueryPort returnPort;

    public ReturningApiServiceAdapter(final ReturnQueryPort returnPort) {
        this.returnPort = returnPort;
    }

    @Override
    public Optional<ReturnDetailsDto> findByShipmentId(final ShipmentId shipmentId) {
        return returnPort.findByShipmentId(shipmentId);
    }
}
