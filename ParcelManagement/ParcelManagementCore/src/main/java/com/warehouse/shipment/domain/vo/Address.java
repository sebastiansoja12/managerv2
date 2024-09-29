package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.model.ShipmentUpdate;

public class Address {
    private final String city;
    private final String street;
    private final String postalCode;

    public Address(final String city, final String street, final String postalCode) {
        this.city = city;
        this.street = street;
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getPostalCode() {
        return postalCode;
    }
    
    public static Address from(final ShipmentUpdate shipmentUpdate) {
		return new Address(shipmentUpdate.getRecipientCity(), shipmentUpdate.getRecipientStreet(),
				shipmentUpdate.getRecipientPostalCode());
    }

    public static Address from(final Person person) {
        return new Address(person.city(), person.street(), person.postalCode());
    }
}
