package com.warehouse.reroute.domain.model;


import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.domain.vo.ParcelId;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {

	private ParcelId parcelId;

	private Sender sender;

	private Recipient recipient;

	private Size parcelSize;

	private Status parcelStatus;

	private ParcelType parcelType;

	private Long parcelRelatedId;

	private String destination;

	public boolean isParent() {
		return parcelType.equals(ParcelType.PARENT);
	}

	public boolean hasStatusCreated() {
		return parcelStatus.equals(Status.CREATED);
	}

	public boolean isRequiredToReroute() {
		return hasStatusCreated() && isParent();
	}
}
