package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.vo.Recipient;
import com.warehouse.shipment.domain.vo.Sender;

public class ShipmentUpdate {

    private Sender sender;
    
    private Recipient recipient;
    
    private String token;

	public ShipmentUpdate(final Sender sender,
                          final Recipient recipient,
                          final String token) {
        this.sender = sender;
        this.recipient = recipient;
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

}
