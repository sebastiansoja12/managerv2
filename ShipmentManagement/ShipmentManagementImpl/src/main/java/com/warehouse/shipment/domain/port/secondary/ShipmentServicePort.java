package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.ShipmentCreateResponse;

public interface ShipmentServicePort {

    ShipmentCreateResponse registerParcel(Long parcelId, String paymentUrl);
}
