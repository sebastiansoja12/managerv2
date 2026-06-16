package com.warehouse.shipment.domain.vo;

import java.time.LocalDateTime;

import com.warehouse.commonassets.enumeration.ShipmentSize;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.DepartmentId;

import lombok.Builder;


@Builder
public class Parcel {
    
    private final Sender sender;

    private final Recipient recipient;

    private final ShipmentSize shipmentSize;

    private final DepartmentId targetDepartmentId;

    private final ShipmentStatus shipmentStatus;

    private final ShipmentType shipmentType;

    private final ShipmentId shipmentRelatedId;

    private final double price;

    private final LocalDateTime createdAt;

    private final LocalDateTime updatedAt;

    private final Boolean locked;

	public Parcel(final Sender sender,
                  final Recipient recipient,
                  final ShipmentSize shipmentSize,
                  final DepartmentId targetDepartmentId,
                  final ShipmentStatus shipmentStatus,
                  final ShipmentType shipmentType,
                  final ShipmentId shipmentRelatedId,
                  final double price,
                  final LocalDateTime createdAt,
                  final LocalDateTime updatedAt,
                  final Boolean locked) {
		this.sender = sender;
		this.recipient = recipient;
		this.shipmentSize = shipmentSize;
		this.targetDepartmentId = targetDepartmentId;
		this.shipmentStatus = shipmentStatus;
		this.shipmentType = shipmentType;
		this.shipmentRelatedId = shipmentRelatedId;
		this.price = price;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.locked = locked;
	}
    
    public Sender getSender() {
        return sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public ShipmentSize getShipmentSize() {
        return shipmentSize;
    }

    public DepartmentId getTargetDepartmentId() {
        return targetDepartmentId;
    }

    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public ShipmentType getShipmentType() {
        return shipmentType;
    }

    public ShipmentId getShipmentRelatedId() {
        return shipmentRelatedId;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Boolean getLocked() {
        return locked;
    }
}
