package com.warehouse.shipment.application.port.primary;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.shipment.application.port.secondary.BillingCustomerServicePort;
import com.warehouse.shipment.application.port.secondary.ShipmentActorServicePort;
import com.warehouse.shipment.domain.exception.BillingCustomerNotActiveException;
import com.warehouse.shipment.domain.vo.ShipmentBillingCustomer;

public class ShipmentCustomerPortImpl implements ShipmentCustomerPort {

    private final ShipmentActorServicePort shipmentActorServicePort;

    private final BillingCustomerServicePort billingCustomerServicePort;

    public ShipmentCustomerPortImpl(final ShipmentActorServicePort shipmentActorServicePort,
                                    final BillingCustomerServicePort billingCustomerServicePort) {
        this.shipmentActorServicePort = shipmentActorServicePort;
        this.billingCustomerServicePort = billingCustomerServicePort;
    }

    @Override
    public BillingCustomerId currentActiveCustomer() {
        return this.billingCustomerServicePort.findByUser(this.shipmentActorServicePort.currentUserId())
                .map(ShipmentBillingCustomer::activeId)
                .orElseThrow(BillingCustomerNotActiveException::new);
    }
}
