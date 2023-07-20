package com.warehouse.parcelstate.domain.model;




import com.warehouse.parcelstate.domain.enumeration.ParcelType;
import com.warehouse.parcelstate.domain.enumeration.Size;
import com.warehouse.parcelstate.domain.enumeration.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {

	ParcelId parcelId;
	Sender sender;
	Recipient recipient;
	Size parcelSize;

	Status status;

	ParcelType parcelType;

	Long parcelRelatedId;

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
