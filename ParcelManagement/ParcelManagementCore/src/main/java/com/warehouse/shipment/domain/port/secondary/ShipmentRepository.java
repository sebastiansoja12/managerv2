package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.ShipmentParcel;
import com.warehouse.shipment.domain.model.UpdateParcelResponse;

public interface ShipmentRepository {

    Parcel save(ShipmentParcel parcel);

    void delete(Long parcelId);

    Parcel loadParcelById(Long parcelId);

    UpdateParcelResponse update(Parcel parcelUpdate);
}
