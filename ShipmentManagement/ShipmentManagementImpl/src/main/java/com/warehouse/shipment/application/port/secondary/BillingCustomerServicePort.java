package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.vo.ShipmentBillingCustomer;

import java.util.Optional;

public interface BillingCustomerServicePort {

    Optional<ShipmentBillingCustomer> findByUser(final UserId userId);
}
