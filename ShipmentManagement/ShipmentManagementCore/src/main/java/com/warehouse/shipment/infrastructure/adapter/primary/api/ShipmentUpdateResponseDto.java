package com.warehouse.shipment.infrastructure.adapter.primary.api;

public class ShipmentUpdateResponseDto {
    private final ShipmentIdDto parcelId;
    private final PersonDto sender;
    private final PersonDto recipient;

    public ShipmentUpdateResponseDto(final ShipmentIdDto parcelId, final PersonDto sender, final PersonDto recipient) {
        this.parcelId = parcelId;
        this.sender = sender;
        this.recipient = recipient;
    }

    public ShipmentIdDto getParcelId() {
        return parcelId;
    }

    public PersonDto getSender() {
        return sender;
    }

    public PersonDto getRecipient() {
        return recipient;
    }
}
