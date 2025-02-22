package com.warehouse.logistics.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DeliveryTokenResponse {
	List<SupplierSignature> supplierSignature;
	String supplierCode;
}
