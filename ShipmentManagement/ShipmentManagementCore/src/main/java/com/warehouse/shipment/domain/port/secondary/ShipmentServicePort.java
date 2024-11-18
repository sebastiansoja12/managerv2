package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.vo.ShipmentResponse;

public interface ShipmentServicePort {

    ShipmentResponse registerParcel(Long parcelId, String paymentUrl);
}
