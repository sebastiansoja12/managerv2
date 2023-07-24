package com.warehouse.shipment.infrastructure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShipmentParcelDto {

    SenderDto sender;

    RecipientDto recipient;

    ParcelSizeDto parcelSize;

    String destination;

    StatusDto status;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;

    double price;
}
