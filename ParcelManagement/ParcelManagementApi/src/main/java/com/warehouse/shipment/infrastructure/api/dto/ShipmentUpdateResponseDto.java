package com.warehouse.shipment.infrastructure.api.dto;

public class ShipmentUpdateResponseDto {
    private final ParcelIdDto parcelId;
    private final PersonDto sender;
    private final PersonDto recipient;

    public ShipmentUpdateResponseDto(final ParcelIdDto parcelId, final PersonDto sender, final PersonDto recipient) {
        this.parcelId = parcelId;
        this.sender = sender;
        this.recipient = recipient;
    }

    public ParcelIdDto getParcelId() {
        return parcelId;
    }

    public PersonDto getSender() {
        return sender;
    }

    public PersonDto getRecipient() {
        return recipient;
    }
}
