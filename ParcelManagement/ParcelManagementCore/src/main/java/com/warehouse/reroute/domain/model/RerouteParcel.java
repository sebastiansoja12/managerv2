package com.warehouse.reroute.domain.model;


import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;

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

	Status status;

	ParcelType parcelType;

	Long parcelRelatedId;

	String destination;

	public boolean isParent() {
		return parcelType.equals(ParcelType.PARENT);
	}

	public boolean hasStatusCreated() {
		return status.equals(Status.CREATED);
	}

	public boolean isRequiredToReroute() {
		return hasStatusCreated() && isParent();
	}
}
