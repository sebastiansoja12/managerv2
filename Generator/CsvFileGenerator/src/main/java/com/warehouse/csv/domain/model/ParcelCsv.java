package com.warehouse.csv.domain.model;

import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParcelCsv {

    Long id;
    String senderFirstName;
    String senderLastName;
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

    Size parcelSize;

    double price;
}
