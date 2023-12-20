package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentParcel {

    private Sender sender;

    private Recipient recipient;

    private Size parcelSize;

    private String destination;

    private Status status;

    private ParcelType parcelType;

    private Long parcelRelatedId;

    private double price;

    public double getPrice() {
        return this.price = parcelSize.getPrice();
    }
}
