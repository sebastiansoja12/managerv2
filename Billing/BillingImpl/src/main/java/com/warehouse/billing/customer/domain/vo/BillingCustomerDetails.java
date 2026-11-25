package com.warehouse.billing.customer.domain.vo;

public record BillingCustomerDetails(String companyName, BillingCustomerAddress address, String invoiceEmail) {
}
