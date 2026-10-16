package com.warehouse.qrcode.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Shipment {

    private Long id;

    private String firstName;

    private String lastName;

    private String senderTelephone;

    private String senderEmail;

    private String senderCity;

    private String senderStreet;

    private String senderPostalCode;

    private String recipientEmail;

    private String recipientTelephone;

    private String recipientFirstName;

    private String recipientLastName;

    private String recipientCity;

    private String recipientStreet;

    private String recipientPostalCode;

    private String destination;
}
