package com.warehouse.deliverytoken.domain.model;

import com.warehouse.deliverytoken.domain.enumeration.ParcelType;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Parcel {
    Long id;

    Long parcelRelatedId;

    ParcelType parcelType;

    String destination;
}
