package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.*;

public interface ShipmentServicePort {

    ShipmentResponse registerParcel(Long parcelId, String paymentUrl);

    UpdateParcelResponse update(ParcelUpdate parcelUpdate);
}
