package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.vo.ShipmentRequest;
import com.warehouse.shipment.domain.vo.ShipmentResponse;

public interface ShipmentPort {

    ShipmentResponse ship(ShipmentRequest request);

    Parcel loadParcel(Long parcelId);

    boolean exists(Long parcelId);
}
