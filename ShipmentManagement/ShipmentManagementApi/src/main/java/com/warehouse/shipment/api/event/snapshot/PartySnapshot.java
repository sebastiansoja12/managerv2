package com.warehouse.shipment.api.event.snapshot;

public record PartySnapshot(
        String firstName,
        String lastName,
        String email,
        String telephoneNumber,
        String city,
        String postalCode,
        String street,
        String countryCode
) {
}
