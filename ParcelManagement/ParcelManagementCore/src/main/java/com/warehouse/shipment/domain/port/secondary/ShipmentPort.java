package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.*;

public interface ShipmentPort {

    ShipmentResponse ship(ShipmentRequest request);

    UpdateParcelResponse update(ParcelUpdate parcelUpdate);
}
