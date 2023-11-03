package com.warehouse.delivery.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class SupplierSignature {
    UUID deliveryId;
    String token;
}
