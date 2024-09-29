package com.warehouse.shipment.infrastructure.api.dto;

public class ShipmentParcelDto {

    private final SenderDto sender;

    private final RecipientDto recipient;

    private final ParcelSizeDto parcelSize;

    private final String destination;

    private final StatusDto status;

    private final ParcelTypeDto parcelType;

    private final ParcelIdDto parcelRelatedId;

    private final double price;

	public ShipmentParcelDto(final SenderDto sender, final RecipientDto recipient, final ParcelSizeDto parcelSize,
			final String destination, final StatusDto status, final ParcelTypeDto parcelType,
			final ParcelIdDto parcelRelatedId, final double price) {
		this.sender = sender;
		this.recipient = recipient;
		this.parcelSize = parcelSize;
		this.destination = destination;
		this.status = status;
		this.parcelType = parcelType;
		this.parcelRelatedId = parcelRelatedId;
		this.price = price;
	}

    public SenderDto getSender() {
        return sender;
    }

    public RecipientDto getRecipient() {
        return recipient;
    }

    public ParcelSizeDto getParcelSize() {
        return parcelSize;
    }

    public String getDestination() {
        return destination;
    }

    public StatusDto getStatus() {
        return status;
    }

    public ParcelTypeDto getParcelType() {
        return parcelType;
    }

    public ParcelIdDto getParcelRelatedId() {
        return parcelRelatedId;
    }

    public double getPrice() {
        return price;
    }
}
