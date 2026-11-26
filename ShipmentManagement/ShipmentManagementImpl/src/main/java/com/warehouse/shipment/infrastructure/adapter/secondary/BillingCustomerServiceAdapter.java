package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.api.dto.BillingCustomerStatusDto;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.application.port.secondary.BillingCustomerServicePort;
import com.warehouse.shipment.domain.vo.ShipmentBillingCustomer;

import java.util.Optional;

public class BillingCustomerServiceAdapter implements BillingCustomerServicePort {

    private final BillingCustomerApiService billingCustomerApiService;

    public BillingCustomerServiceAdapter(final BillingCustomerApiService billingCustomerApiService) {
        this.billingCustomerApiService = billingCustomerApiService;
    }

    @Override
    public Optional<ShipmentBillingCustomer> findByUser(final UserId userId) {
        return this.billingCustomerApiService.findByUser(userId)
                .map(customer -> new ShipmentBillingCustomer(customer.billingCustomerId(),
                        BillingCustomerStatusDto.ACTIVE.equals(customer.status())));
    }
}
