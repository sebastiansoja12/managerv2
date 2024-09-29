package com.warehouse.shipment.domain.vo;

import lombok.Builder;

@Builder
public class Recipient implements Person {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String city;
    private final String postalCode;
    private final String street;

	public Recipient(final String firstName, final String lastName, final String email, final String telephoneNumber,
			final String city, final String postalCode, final String street) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
    }

    @Override
    public String firstName() {
        return firstName;
    }

    @Override
    public String lastName() {
        return lastName;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String telephoneNumber() {
        return telephoneNumber;
    }

    @Override
    public String city() {
        return city;
    }

    @Override
    public String postalCode() {
        return postalCode;
    }

    @Override
    public String street() {
        return street;
    }
}