package com.warehouse.routetracker.infrastructure.adapter.primary.kafka.event.snapshot;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SenderSnapshot(
        String firstName,
        String lastName,
        String email,
        String telephoneNumber,
        String city,
        String postalCode,
        String street
) {
}
