package com.warehouse.reroute.domain.model;


import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RerouteParcel {

	Sender sender;

	Recipient recipient;

	Size parcelSize;

	Status parcelStatus;

	ParcelType parcelType;

	Long parcelRelatedId;

	String destination;

	public boolean isParent() {
		return parcelType.equals(ParcelType.PARENT);
	}

	public boolean hasStatusCreated() {
		return parcelStatus.equals(Status.CREATED);
	}

	public boolean hasStatusReroute() {
		return parcelStatus.equals(Status.REROUTE);
	}

	public boolean hasStatusCreatedOrReroute() {
		return hasStatusCreated() || hasStatusReroute();
	}

	public boolean isRequiredToReroute() {
		return hasStatusCreatedOrReroute() && isParent();
	}
}
