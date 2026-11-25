package com.warehouse.billing.payment.application.port.primary.result;

import com.warehouse.billing.payment.domain.model.Payment;

import java.util.List;

public record PaymentPage(List<Payment> payments,
                          int page,
                          int size,
                          long totalElements) {

    public PaymentPage {
        payments = List.copyOf(payments);
    }
}
