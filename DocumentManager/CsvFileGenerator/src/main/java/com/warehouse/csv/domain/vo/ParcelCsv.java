package com.warehouse.csv.domain.vo;

import lombok.*;

@Value
@Builder
public class ParcelCsv {
    Long id;
    String firstName;
    String lastName;
    String senderEmail;
    String senderTelephoneNumber;
    String senderCity;
    String senderPostalCode;
    String senderStreet;
    String recipientFirstName;
    String recipientLastName;
    String recipientEmail;
    String recipientTelephoneNumber;
    String recipientCity;
    String recipientPostalCode;
    String recipientStreet;
}
