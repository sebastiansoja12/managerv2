package com.warehouse.suppliertoken.domain.model;

import com.warehouse.suppliertoken.domain.enumeration.ParcelType;
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
