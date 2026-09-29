package com.warehouse.shipment.domain.vo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warehouse.commonassets.enumeration.CountryCode;
import lombok.Builder;

@Builder
public class Party {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String city;
    private final String postalCode;
    private final String street;
    private final CountryCode countryCode;

    @JsonCreator
    public Party(@JsonProperty("firstName") final String firstName,
                 @JsonProperty("lastName") final String lastName,
                 @JsonProperty("email") final String email,
                 @JsonProperty("telephoneNumber") final String telephoneNumber,
                 @JsonProperty("city") final String city,
                 @JsonProperty("postalCode") final String postalCode,
                 @JsonProperty("street") final String street,
                 @JsonProperty("countryCode") final CountryCode countryCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.countryCode = countryCode;
    }

    public Party withCountryCode(final CountryCode countryCode) {
        return new Party(firstName, lastName, email, telephoneNumber, city, postalCode, street, countryCode);
    }

    public Address getAddress() {
        return new Address(city, street, postalCode, countryCode);
    }

    public CountryCode getCountryCode() {
        return countryCode;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }
}
