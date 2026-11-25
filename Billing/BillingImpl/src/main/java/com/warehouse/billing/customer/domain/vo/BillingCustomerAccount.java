package com.warehouse.billing.customer.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record BillingCustomerAccount(UserId userId, OperatorId operatorId) {
}
