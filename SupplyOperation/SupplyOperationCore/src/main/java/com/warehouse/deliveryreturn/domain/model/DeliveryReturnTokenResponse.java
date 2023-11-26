package com.warehouse.deliveryreturn.domain.model;

import java.util.List;

import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnSignature;

import lombok.Value;

@Value
public class DeliveryReturnTokenResponse {
    List<DeliveryReturnSignature> deliveryReturnSignatures;
    String supplierCode;
}
