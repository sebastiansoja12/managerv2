package com.warehouse.shipment.domain.vo;

import com.warehouse.shipment.domain.enumeration.PersonType;

public interface Person {
    String getFirstName();
    String getLastName();
    String getEmail();
    String getTelephoneNumber();
    String getCity();
    String getPostalCode();
    String getStreet();
    PersonType getType();
}
