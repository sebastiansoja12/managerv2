package com.warehouse.route.domain.model;

import com.warehouse.route.domain.enumeration.ParcelType;
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
}
