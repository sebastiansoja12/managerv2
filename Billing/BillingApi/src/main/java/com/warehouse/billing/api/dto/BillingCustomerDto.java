package com.warehouse.billing.api.dto;

import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;

public record BillingCustomerDto(BillingCustomerId billingCustomerId,
                                 OperatorId operatorId,
                                 String companyName,
                                 TaxId taxId,
                                 String street,
                                 String postalCode,
                                 String city,
                                 String countryCode,
                                 String invoiceEmail,
                                 BillingCustomerStatusDto status,
                                 UserId userId) {
}
