package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.BillingCustomerId;

public interface ShipmentCustomerPort {

    BillingCustomerId currentActiveCustomer();
}
