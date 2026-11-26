package com.warehouse.billing.api;

import com.warehouse.billing.api.dto.BillingCustomerDto;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public interface BillingCustomerApiService {

    Optional<BillingCustomerDto> findByUser(final UserId userId);

    Optional<BillingCustomerDto> findById(final BillingCustomerId billingCustomerId);
}
