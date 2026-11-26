package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.shipment.domain.exception.BillingCustomerNotActiveException;

public record ShipmentBillingCustomer(BillingCustomerId billingCustomerId, boolean active) {

    public BillingCustomerId activeId() {
        if (!this.active) {
            throw new BillingCustomerNotActiveException();
        }
        return this.billingCustomerId;
    }
}
