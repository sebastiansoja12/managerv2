package com.warehouse.billing.customer.domain.service;

import com.warehouse.billing.customer.domain.enumeration.BillingCustomerStatus;
import com.warehouse.billing.customer.domain.model.BillingCustomer;
import com.warehouse.billing.customer.domain.vo.BillingCustomerAccount;
import com.warehouse.billing.customer.domain.vo.BillingCustomerDetails;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;

import java.time.Instant;

public class BillingCustomerFactory {

    private static final String USERNAME_PREFIX = "shop-";

    private static final String EMAIL_DOMAIN = "@billing.invalid";

    public BillingCustomer create(final TaxId taxId, final BillingCustomerDetails details,
                                  final BillingCustomerAccount account, final Instant now) {
        return new BillingCustomer(BillingCustomerId.nextId(), account.operatorId(), taxId, account.userId(),
                details, BillingCustomerStatus.ACTIVE, now, now);
    }

    public String username(final OperatorId operatorId, final TaxId taxId) {
        return USERNAME_PREFIX + operatorId.value() + "-" + taxId.value();
    }

    public String userEmail(final OperatorId operatorId, final TaxId taxId) {
        return username(operatorId, taxId) + EMAIL_DOMAIN;
    }
}
