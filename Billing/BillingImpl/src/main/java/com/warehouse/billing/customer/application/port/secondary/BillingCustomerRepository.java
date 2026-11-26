package com.warehouse.billing.customer.application.port.secondary;

import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface BillingCustomerRepository {

    void create(final BillingCustomer billingCustomer);

    void update(final BillingCustomer billingCustomer);

    Optional<BillingCustomer> findById(final BillingCustomerId billingCustomerId);

    Optional<BillingCustomer> findByUser(final UserId userId);

    Optional<BillingCustomer> findByTaxId(final TaxId taxId);

    List<BillingCustomer> findAll();
}
