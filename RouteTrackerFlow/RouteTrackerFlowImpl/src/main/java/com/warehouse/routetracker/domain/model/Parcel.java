package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.domain.enumeration.ParcelSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {
    private Long id;
    private Sender sender;
    private Recipient recipient;
    private ParcelSize parcelSize;
}
