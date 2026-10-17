package com.warehouse.reroute.api.event.snapshot;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ReroutePartyData(String firstName,
                               String lastName,
                               String email,
                               String telephoneNumber,
                               String city,
                               String postalCode,
                               String street,
                               String countryCode) {

    @JsonCreator
    public ReroutePartyData(@JsonProperty("firstName") final String firstName,
                            @JsonProperty("lastName") final String lastName,
                            @JsonProperty("email") final String email,
                            @JsonProperty("telephoneNumber") final String telephoneNumber,
                            @JsonProperty("city") final String city,
                            @JsonProperty("postalCode") final String postalCode,
                            @JsonProperty("street") final String street,
                            @JsonProperty("countryCode") final String countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.countryCode = countryCode;
    }
}
