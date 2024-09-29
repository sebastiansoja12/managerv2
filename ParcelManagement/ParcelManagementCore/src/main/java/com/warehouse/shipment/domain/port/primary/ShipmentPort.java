package com.warehouse.shipment.domain.port.primary;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.*;

public interface ShipmentPort {

    ShipmentResponse ship(final ShipmentRequest request);

    ShipmentUpdateResponse update(final ShipmentUpdateRequest request);

    Parcel loadParcel(final ParcelId parcelId);

    boolean exists(final Long parcelId);
}
