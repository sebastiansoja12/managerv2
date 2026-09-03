package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
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
