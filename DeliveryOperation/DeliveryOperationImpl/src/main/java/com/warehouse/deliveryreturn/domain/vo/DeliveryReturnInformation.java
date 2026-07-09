package com.warehouse.deliveryreturn.domain.vo;

import java.util.UUID;

import lombok.Builder;

@Builder
public class DeliveryReturnInformation {
	private final UUID id;
	private final Long shipmentId;
	private final String departmentCode;
	private final String deliveryStatus;
	private final String token;
	private final Boolean locked;

	public DeliveryReturnInformation(final UUID id, final Long shipmentId, final String departmentCode,
			final String deliveryStatus, final String token, final Boolean locked) {
		this.id = id;
		this.shipmentId = shipmentId;
		this.departmentCode = departmentCode;
		this.deliveryStatus = deliveryStatus;
		this.token = token;
		this.locked = locked;
	}

	public UUID getId() {
		return id;
	}

	public Long getShipmentId() {
		return shipmentId;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public String getDeliveryStatus() {
		return deliveryStatus;
	}

	public String getToken() {
		return token;
	}

	public Boolean getLocked() {
		return locked;
	}
}
