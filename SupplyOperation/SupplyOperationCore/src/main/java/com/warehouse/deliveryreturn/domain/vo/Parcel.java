package com.warehouse.deliveryreturn.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Parcel {
    Long id;
    String senderEmail;
    String recipientEmail;
    String parcelStatus;
}
