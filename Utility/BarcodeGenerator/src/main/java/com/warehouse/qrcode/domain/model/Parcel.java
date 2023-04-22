package com.warehouse.qrcode.domain.model;

import com.warehouse.shipment.domain.enumeration.Size;
import com.warehouse.shipment.domain.model.Recipient;
import com.warehouse.shipment.domain.model.Sender;
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

    double price;

    public double price() {
        return parcelSize.getPrice();
    }
}