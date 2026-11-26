package com.warehouse.billing.customer.application.port.primary;

import com.warehouse.billing.customer.application.port.primary.command.CreateBillingCustomerCommand;
import com.warehouse.billing.customer.application.port.primary.command.UpdateBillingCustomerCommand;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;
import java.util.Optional;

public interface BillingCustomerPort {

    BillingCustomer create(final CreateBillingCustomerCommand command);

    BillingCustomer update(final UpdateBillingCustomerCommand command);

    List<BillingCustomer> findAll();

    BillingCustomer get(final BillingCustomerId billingCustomerId);

    Optional<BillingCustomer> findById(final BillingCustomerId billingCustomerId);

    Optional<BillingCustomer> findByUser(final UserId userId);

    BillingCustomer currentActive();

    String regenerateApiKey(final BillingCustomerId billingCustomerId);

    BillingCustomer block(final BillingCustomerId billingCustomerId);

    BillingCustomer unblock(final BillingCustomerId billingCustomerId);
}
