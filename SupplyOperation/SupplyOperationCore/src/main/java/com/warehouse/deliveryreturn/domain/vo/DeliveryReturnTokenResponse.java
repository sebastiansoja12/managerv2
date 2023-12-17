package com.warehouse.deliveryreturn.domain.vo;

import java.util.List;

import lombok.Value;

@Value
public class DeliveryReturnTokenResponse {
    List<DeliveryReturnSignature> deliveryReturnSignatures;
    String supplierCode;
}
