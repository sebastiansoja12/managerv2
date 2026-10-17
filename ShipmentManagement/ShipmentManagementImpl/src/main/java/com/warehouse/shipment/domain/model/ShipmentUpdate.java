package com.warehouse.shipment.domain.model;

import com.warehouse.shipment.domain.vo.Party;

public class ShipmentUpdate {

    private Party sender;
    
    private Party recipient;
    
    private String token;

	public ShipmentUpdate(final Party sender,
                          final Party recipient,
                          final String token) {
        this.sender = sender;
        this.recipient = recipient;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public Party getSender() {
        return sender;
    }

    public Party getRecipient() {
        return recipient;
    }

}
