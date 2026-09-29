package com.warehouse.shipment.application.event.snapshot;

import com.warehouse.shipment.domain.vo.Party;

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

    public static PartySnapshot from(final Party party) {
        if (party == null) {
            return null;
        }
        return new PartySnapshot(
                party.getFirstName(),
                party.getLastName(),
                party.getEmail(),
                party.getTelephoneNumber(),
                party.getCity(),
                party.getPostalCode(),
                party.getStreet(),
                party.getAddress().getCountryCode() == null ? null : party.getAddress().getCountryCode().name()
        );
    }
}
