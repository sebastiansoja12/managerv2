package com.warehouse.shipment.domain.event;

import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.domain.enumeration.ReasonCode;
import com.warehouse.shipment.domain.vo.ShipmentSnapshot;

public class ShipmentReturnCreated extends ShipmentStatusChangedEvent implements ShipmentEvent {
    
    private final ReasonCode reasonCode;
    private final String reason;
    private final DepartmentCode departmentCode;
    
    public ShipmentReturnCreated(final ShipmentSnapshot snapshot, 
                                 final ReasonCode reasonCode, final String reason,
                                 final DepartmentCode departmentCode,
                                 final Instant timestamp) {
        super(snapshot, timestamp);
        this.reason = reason;
        this.reasonCode = reasonCode;
        this.departmentCode = departmentCode;
    }
    
    public ReasonCode getReasonCode() {
        return reasonCode;
    }
    public String getReason() {
        return reason;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }
}
