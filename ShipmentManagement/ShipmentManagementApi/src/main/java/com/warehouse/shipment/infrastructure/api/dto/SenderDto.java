package com.warehouse.shipment.infrastructure.api.dto;

import lombok.Builder;

@Builder
public class SenderDto {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String city;
    private final String postalCode;
    private final String street;

	public SenderDto(final String firstName, final String lastName, final String email, final String telephoneNumber,
			final String city, final String postalCode, final String street) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.city = city;
		this.postalCode = postalCode;
		this.street = street;
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
