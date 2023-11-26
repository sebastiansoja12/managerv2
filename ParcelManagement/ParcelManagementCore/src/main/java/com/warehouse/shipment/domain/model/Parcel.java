package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.domain.enumeration.ParcelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {

    private Long id;

    private Sender sender;

    private Recipient recipient;

    private Size parcelSize;

    private String destination;

    private Status parcelStatus;

    private ParcelType parcelType;

    private Long parcelRelatedId;

    private double price;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public double getPrice() {
        return this.price = parcelSize.getPrice();
    }
}
