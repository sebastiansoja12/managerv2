package com.warehouse.shipment.infrastructure.adapter.primary.api;

public class ShipmentUpdateResponseDto {
    private final ShipmentIdDto parcelId;
    private final PersonApi sender;
    private final PersonApi recipient;

    public ShipmentUpdateResponseDto(final ShipmentIdDto parcelId, final PersonApi sender, final PersonApi recipient) {
        this.parcelId = parcelId;
        this.sender = sender;
        this.recipient = recipient;
    }

    public ShipmentIdDto getParcelId() {
        return parcelId;
    }

    public PersonApi getSender() {
        return sender;
    }

    public PersonApi getRecipient() {
        return recipient;
    }
}
