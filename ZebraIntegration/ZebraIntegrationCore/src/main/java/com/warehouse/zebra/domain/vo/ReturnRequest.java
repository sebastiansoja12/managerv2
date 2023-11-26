package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ReturnRequest {

	Parcel parcel;

	String reason;

	ReturnStatus returnStatus;

	String returnToken;

	String supplierCode;
}
