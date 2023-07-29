package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ParcelUpdate {
    Long id;

    String senderFirstName;

    String senderLastName;

    String senderTelephone;

    String senderEmail;

    String senderCity;

    String senderStreet;

    String senderPostalCode;

    String recipientFirstName;

    String recipientLastName;

    String recipientTelephone;

    String recipientEmail;

    String recipientCity;

    String recipientStreet;

    String recipientPostalCode;

    String destination;

    Size parcelSize;

    Status status;

    ParcelType parcelType;
}
