package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.enumeration.Size;
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
    Size parcelSize;

    String destination;

    double price;

    String status;

    public double getPrice() {
        return this.price = parcelSize.getPrice();
    }
}
