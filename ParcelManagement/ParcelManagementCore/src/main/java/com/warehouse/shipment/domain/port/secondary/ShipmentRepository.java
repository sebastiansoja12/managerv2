package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.commonassets.identificator.ParcelId;
import com.warehouse.shipment.domain.vo.Parcel;
import com.warehouse.shipment.domain.model.ShipmentUpdate;
import com.warehouse.shipment.domain.model.ShipmentParcel;

public interface ShipmentRepository {

    Parcel save(ShipmentParcel parcel);

    Parcel update(ShipmentUpdate parcel);

    Parcel findParcelById(final ParcelId parcelId);

    boolean exists(Long parcelId);
}
