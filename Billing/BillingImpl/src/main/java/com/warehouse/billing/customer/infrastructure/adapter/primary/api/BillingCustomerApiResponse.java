package com.warehouse.billing.customer.infrastructure.adapter.primary.api;

import com.warehouse.billing.customer.domain.enumeration.BillingCustomerStatus;

import java.time.Instant;

public record BillingCustomerApiResponse(BillingCustomerIdDto billingCustomerId,
                                         String companyName,
                                         String taxId,
                                         String street,
                                         String postalCode,
                                         String city,
                                         String countryCode,
                                         String invoiceEmail,
                                         BillingCustomerStatus status,
                                         UserIdDto userId,
                                         Instant createdAt,
                                         Instant updatedAt) {
}
