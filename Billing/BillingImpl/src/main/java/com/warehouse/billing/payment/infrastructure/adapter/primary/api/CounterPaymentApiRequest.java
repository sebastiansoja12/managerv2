package com.warehouse.billing.payment.infrastructure.adapter.primary.api;

import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CounterPaymentApiRequest(@NotNull PaymentMethod method,
                                       @Size(max = 32) String buyerTaxId) {
}
