package com.warehouse.delivery.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DeliveryTokenResponse {
	List<SupplierSignature> supplierSignature;
	String supplierCode;
}
