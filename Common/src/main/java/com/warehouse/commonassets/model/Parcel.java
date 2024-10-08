package com.warehouse.commonassets.model;

import com.warehouse.commonassets.enumeration.ParcelStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.enumeration.Size;

public class Parcel {

	private Long id;

	private Size parcelSize;

	private String destination;

	private ParcelStatus status;

	private ShipmentType shipmentType;

	private Long parcelRelatedId;

	public Parcel(Long id, Size parcelSize, String destination, ParcelStatus status, ShipmentType shipmentType, Long parcelRelatedId) {
		this.id = id;
		this.parcelSize = parcelSize;
		this.destination = destination;
		this.status = status;
		this.shipmentType = shipmentType;
		this.parcelRelatedId = parcelRelatedId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Size getParcelSize() {
		return parcelSize;
	}

	public void setParcelSize(Size parcelSize) {
		this.parcelSize = parcelSize;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public ParcelStatus getStatus() {
		return status;
	}

	public void setStatus(ParcelStatus status) {
		this.status = status;
	}

	public ShipmentType getParcelType() {
		return shipmentType;
	}

	public void setParcelType(ShipmentType shipmentType) {
		this.shipmentType = shipmentType;
	}

	public Long getParcelRelatedId() {
		return parcelRelatedId;
	}

	public void setParcelRelatedId(Long parcelRelatedId) {
		this.parcelRelatedId = parcelRelatedId;
	}
}
