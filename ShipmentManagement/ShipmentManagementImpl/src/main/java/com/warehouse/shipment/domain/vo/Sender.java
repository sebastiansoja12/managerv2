package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.domain.model.Shipment;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PersonApi;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ShipmentEntity;
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

    public static Sender from(final ShipmentEntity entity) {
        final String firstName = entity.getFirstName();
        final String lastName = entity.getLastName();
        final String email = entity.getSenderEmail();
        final String telephoneNumber = entity.getSenderTelephone();
        final String city = entity.getSenderCity();
        final String postalCode = entity.getSenderPostalCode();
        final String street = entity.getSenderStreet();
        return new Sender(firstName, lastName, email, telephoneNumber, city, postalCode, street);
    }

	public static Person from(final PersonApi request) {
		return new Sender(request.getFirstName(), request.getLastName(), request.getEmail(),
				request.getTelephoneNumber(), request.getCity(), request.getPostalCode(), request.getStreet());
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