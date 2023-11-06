package com.warehouse.deliveryreturn.domain.vo;

import lombok.Value;

import java.util.UUID;

@Value
public class DeliveryReturnResponseDetails {
    UUID id;
    Long parcelId;
    String deliveryStatus;
    String returnToken;
    UpdateStatus updateStatus;
}
