package com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.dto;

import com.warehouse.suppliertoken.infrastructure.adapter.secondary.api.enumeration.ParcelTypeDto;
import lombok.*;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParcelDto {

    ParcelIdDto parcelId;

    ParcelTypeDto parcelType;

    Long parcelRelatedId;

    String destination;
}