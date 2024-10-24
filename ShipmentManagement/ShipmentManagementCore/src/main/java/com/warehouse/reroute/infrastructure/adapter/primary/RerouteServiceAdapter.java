package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.reroute.RerouteService;
import com.warehouse.reroute.domain.port.primary.RerouteTokenPort;
import com.warehouse.shipment.infrastructure.api.dto.ShipmentIdDto;

public class RerouteServiceAdapter implements RerouteService {

    private final RerouteTokenPort rerouteTokenPort;

    public RerouteServiceAdapter(final RerouteTokenPort rerouteTokenPort) {
        this.rerouteTokenPort = rerouteTokenPort;
    }

    @Override
    public void processRerouting(final ShipmentIdDto id) {
        final ShipmentId shipmentId = new ShipmentId(id.getValue());
        this.rerouteTokenPort.invalidateToken(shipmentId);
    }
}
