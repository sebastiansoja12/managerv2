package com.warehouse.reroute.domain.vo;

public record RerouteParty(String firstName,
                           String lastName,
                           String email,
                           String telephoneNumber,
                           String city,
                           String postalCode,
                           String street,
                           String countryCode) {
}
