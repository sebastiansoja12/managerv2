package com.warehouse.shipment.domain.port.primary;

import com.warehouse.shipment.domain.model.*;

public interface ShipmentPort {

    ShipmentResponse ship(ShipmentRequest request);

    void delete(Long parcelId);

    Parcel loadParcel(Long parcelId);

    UpdateParcelResponse update(UpdateParcelRequest updateParcelRequest);
}
