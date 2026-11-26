package com.warehouse.billing.customer.infrastructure.adapter.primary;

import com.warehouse.billing.api.BillingCustomerApiService;
import com.warehouse.billing.api.dto.BillingCustomerDto;
import com.warehouse.billing.customer.application.port.primary.BillingCustomerPort;
import com.warehouse.billing.customer.infrastructure.adapter.primary.mapper.BillingCustomerWebMapper;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public class BillingCustomerApiServiceAdapter implements BillingCustomerApiService {

    private final BillingCustomerPort billingCustomerPort;

    private final BillingCustomerWebMapper billingCustomerWebMapper;

    public BillingCustomerApiServiceAdapter(final BillingCustomerPort billingCustomerPort,
                                            final BillingCustomerWebMapper billingCustomerWebMapper) {
        this.billingCustomerPort = billingCustomerPort;
        this.billingCustomerWebMapper = billingCustomerWebMapper;
    }

    @Override
    public Optional<BillingCustomerDto> findByUser(final UserId userId) {
        return this.billingCustomerPort.findByUser(userId).map(this.billingCustomerWebMapper::toDto);
    }

    @Override
    public Optional<BillingCustomerDto> findById(final BillingCustomerId billingCustomerId) {
        return this.billingCustomerPort.findById(billingCustomerId).map(this.billingCustomerWebMapper::toDto);
    }
}
