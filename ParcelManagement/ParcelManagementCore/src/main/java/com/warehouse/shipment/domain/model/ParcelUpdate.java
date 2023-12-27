package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.enumeration.ParcelType;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParcelUpdate {
    private Long id;

    private String senderFirstName;

    private String senderLastName;

    private String senderTelephone;

    private String senderEmail;

    private String senderCity;

    private String senderStreet;

    private String senderPostalCode;

    private String recipientFirstName;

    private String recipientLastName;

    private String recipientTelephone;

    private String recipientEmail;

    private String recipientCity;

    private String recipientStreet;

    private String recipientPostalCode;

    private String destination;

    private Size parcelSize;

    private Status status;

    private ParcelType parcelType;

    public void updateDestination(String destination) {
        this.destination = destination;
    }
}
