package com.warehouse.create.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Parcel {
    Long id;
    String senderEmail;
    String recipientEmail;
    String parcelStatus;
    Boolean locked;
}
