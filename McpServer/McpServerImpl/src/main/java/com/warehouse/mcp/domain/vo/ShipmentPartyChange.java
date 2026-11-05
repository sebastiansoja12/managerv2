package com.warehouse.mcp.domain.vo;

public record ShipmentPartyChange(String firstName,
                                  String lastName,
                                  String email,
                                  String telephoneNumber,
                                  String city,
                                  String postalCode,
                                  String street,
                                  String countryCode) {

    public ShipmentParty applyTo(final ShipmentParty current) {
        final ShipmentParty party = current != null ? current
                : new ShipmentParty(null, null, null, null, null, null, null, null);
        return new ShipmentParty(
                firstName != null ? firstName : party.firstName(),
                lastName != null ? lastName : party.lastName(),
                email != null ? email : party.email(),
                telephoneNumber != null ? telephoneNumber : party.telephoneNumber(),
                city != null ? city : party.city(),
                postalCode != null ? postalCode : party.postalCode(),
                street != null ? street : party.street(),
                countryCode != null ? countryCode : party.countryCode());
    }
}
