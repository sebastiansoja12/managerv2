package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.UpdateParcelRequest;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;

public interface ShipmentReroutePort {
    UpdateParcelResponse reroute(UpdateParcelRequest updateParcelRequest);

}
