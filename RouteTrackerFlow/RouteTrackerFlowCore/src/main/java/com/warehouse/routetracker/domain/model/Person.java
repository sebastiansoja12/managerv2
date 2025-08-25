package com.warehouse.routetracker.domain.model;

import com.warehouse.routetracker.infrastructure.adapter.primary.api.PersonChangeRequest;

public class Person {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String telephoneNumber;
    private final String city;
    private final String postalCode;
    private final String street;
    private final PersonType personType;
    
	public Person(final String firstName,
                  final String lastName,
                  final String email,
                  final String telephoneNumber,
                  final String city,
                  final String postalCode,
                  final String street,
                  final PersonType personType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.telephoneNumber = telephoneNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.personType = personType;
    }

    public static Person from(final PersonChangeRequest request, final PersonType personType) {
        return new Person(request.firstName(), request.lastName(), request.email(), request.telephoneNumber(), request.city(),
                request.postalCode(), request.street(), personType);
    }

    public String getCity() {
        return city;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public PersonType getPersonType() {
        return personType;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public enum PersonType {
        SENDER, RECIPIENT;
    }
    
}
