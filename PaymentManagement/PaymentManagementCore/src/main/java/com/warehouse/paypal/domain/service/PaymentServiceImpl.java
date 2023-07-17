package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.port.secondary.PaymentSecondaryPort;
import lombok.AllArgsConstructor;

// TODO refactor 2023.9
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentSecondaryPort paymentSecondaryPort;

    @Override
    public PaymentResponse payment(PaymentRequest request) {
        return paymentSecondaryPort.payment(request);
    }

    @Override
    public String update(PaymentInformation paymentInformation) {
        return paymentSecondaryPort.update(paymentInformation.getPaymentId(), paymentInformation.getPayerId());
    }
}
