package com.warehouse.returning.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReturnModel {
	Long shipmentId;
	String reason;
	String returnStatus;
	String returnToken;
	String supplierCode;
	String depotCode;
	String username;
}
