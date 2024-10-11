package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;
import com.warehouse.shipment.domain.vo.ShipmentUpdateRequest;

public class ShipmentUpdate {

    private Sender sender;
    
    private Recipient recipient;
    
    private String destination;

    private String token;

	public ShipmentUpdate(final Sender sender,
                          final Recipient recipient,
                          final String destination,
                          final String token) {
        this.sender = sender;
        this.recipient = recipient;
        this.destination = destination;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Sender getSender() {
        return sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public String getDestination() {
        return destination;
    }

    public void updateDestination(final String destination) {
        this.destination = destination;
    }

    public static ShipmentUpdate from(final ShipmentUpdateRequest request) {
        return request.getShipmentUpdate();
    }
}
