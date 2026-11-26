package com.warehouse.billing.customer.application.port.primary.command;

import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.commonassets.identificator.BillingCustomerId;

public record UpdateBillingCustomerCommand(BillingCustomerId billingCustomerId, BillingCustomerDetails details) {
}
