package com.warehouse.reroute.domain.vo;

import com.warehouse.reroute.domain.enumeration.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParcelUpdateResponse {
    ParcelId parcelId;
    Sender sender;
    Recipient recipient;
    Size parcelSize;
}
