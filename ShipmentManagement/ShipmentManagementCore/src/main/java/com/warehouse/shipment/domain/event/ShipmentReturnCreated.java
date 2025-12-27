package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentReturnCreated extends ShipmentStatusChangedEvent implements ShipmentEvent {
    
    private ReasonCode reasonCode;
    private String reason;
    
    public ShipmentReturnCreated(final ShipmentSnapshot snapshot, 
                                 final ReasonCode reasonCode, final String reason, 
                                 final Instant timestamp) {
        super(snapshot, timestamp);
        this.reason = reason;
        this.reasonCode = reasonCode;
    }
    
    public ReasonCode getReasonCode() {
        return reasonCode;
    }
    public String getReason() {
        return reason;
    }
}
