package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.shipment.domain.vo.*;

public interface ShipmentPort {

    ShipmentResponse ship(final ShipmentRequest request);

    ShipmentUpdateResponse update(final ShipmentUpdateRequest request);

    Parcel loadParcel(final ShipmentId shipmentId);

    boolean exists(final Long parcelId);
}
