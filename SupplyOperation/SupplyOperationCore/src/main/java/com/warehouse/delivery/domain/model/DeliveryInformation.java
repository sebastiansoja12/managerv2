package com.warehouse.delivery.domain.model;

import com.warehouse.delivery.domain.enumeration.DeliveryStatus;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class DeliveryInformation {
    private UUID id;
    private Long parcelId;
    private String depotCode;
    private DeliveryStatus deliveryStatus;
    private String token;
}
