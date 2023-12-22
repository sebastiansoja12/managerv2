package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Objects;

@Value
@Builder
public class ReturnRequest {

	Parcel parcel;

	String reason;

	ReturnStatus returnStatus;

	String returnToken;

	String supplierCode;

	public Long getParcelId() {
		if (ObjectUtils.isNotEmpty(parcel)) {
			return parcel.getId();
		}
		return null;
	}
}
