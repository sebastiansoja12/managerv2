package com.warehouse.delivery.domain.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeliveryResponse {
    UUID id;
    Long parcelId;
    String deliveryStatus;
}
