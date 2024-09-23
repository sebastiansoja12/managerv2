package com.warehouse.deliverymissed.domain.vo;


import com.warehouse.commonassets.enumeration.ParcelStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class UpdateStatusParcelRequest {
    Long parcelId;
    ParcelStatus parcelStatus = ParcelStatus.DELIVERY;
}
