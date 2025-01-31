package com.warehouse.logistics.domain.vo;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class DeliveryTokenRequest {
	List<DeliveryPackageRequest> deliveryPackageRequests;
	Supplier supplier;
}
