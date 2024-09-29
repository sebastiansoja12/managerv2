package com.warehouse.shipment.infrastructure.api.dto;

public final class ShipmentUpdateRequestDto {
	private final ParcelIdDto parcelId;
	private final PersonDto sender;
	private final PersonDto recipient;
    private final String token;

	public ShipmentUpdateRequestDto(final ParcelIdDto parcelId, final PersonDto sender, final PersonDto recipient,
			final String token) {
		this.parcelId = parcelId;
		this.sender = sender;
		this.recipient = recipient;
        this.token = token;
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

    public String getToken() {
        return token;
    }
}
