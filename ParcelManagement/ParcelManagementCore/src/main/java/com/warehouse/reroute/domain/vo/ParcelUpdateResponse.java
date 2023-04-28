package com.warehouse.reroute.domain.vo;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParcelUpdateResponse {
    ParcelId parcelId;
    Sender sender;
    Recipient recipient;
    Size parcelSize;

    Status status;

    ParcelType parcelType;
}
