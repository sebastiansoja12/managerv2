package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.vo.UpdateParcelRequest;
import com.warehouse.shipment.domain.vo.UpdateParcelResponse;

public interface ShipmentReroutePort {
    UpdateParcelResponse reroute(UpdateParcelRequest updateParcelRequest);

}
