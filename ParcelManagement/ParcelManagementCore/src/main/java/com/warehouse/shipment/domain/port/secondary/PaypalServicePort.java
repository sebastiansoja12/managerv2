package com.warehouse.shipment.domain.port.secondary;

import com.warehouse.shipment.domain.model.Parcel;
import com.warehouse.shipment.domain.model.PaymentStatus;

public interface PaypalServicePort {
    PaymentStatus payment(Parcel parcel);
}
