package com.warehouse.deliveryreturn.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class DeliveryReturnSignature {
    UUID deliveryId;
    String token;
}
