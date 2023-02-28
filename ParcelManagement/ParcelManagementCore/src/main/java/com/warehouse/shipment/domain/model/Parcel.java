package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {
    Long id;
    Sender sender;
    Recipient recipient;
    ParcelType parcelType;

    String destination;

    double price;

    public double price() {
        this.price = parcelType.getPrice();
        return this.price;
    }
}
