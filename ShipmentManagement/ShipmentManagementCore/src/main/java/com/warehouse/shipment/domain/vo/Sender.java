package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.model.Shipment;
import lombok.Builder;

import static com.warehouse.shipment.domain.enumeration.PersonType.SENDER;

@Builder
public class Sender implements Person {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String city;
    private final String postalCode;
    private final String street;

	public Sender(final String firstName,
                  final String lastName,
                  final String email,
                  final String telephoneNumber,
                  final String city,
                  final String postalCode,
                  final String street) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.city = city;
		this.postalCode = postalCode;
		this.street = street;
	}

    public static Sender from(final Shipment shipment) {
        return shipment.getSender();
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getPostalCode() {
        return postalCode;
    }

    @Override
    public String getStreet() {
        return street;
    }

    @Override
    public PersonType getType() {
        return SENDER;
    }
}