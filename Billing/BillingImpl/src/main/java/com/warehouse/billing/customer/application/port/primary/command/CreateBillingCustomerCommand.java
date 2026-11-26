package com.warehouse.billing.customer.application.port.primary.command;

import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.commonassets.identificator.TaxId;

public record CreateBillingCustomerCommand(TaxId taxId, BillingCustomerDetails details) {
}
