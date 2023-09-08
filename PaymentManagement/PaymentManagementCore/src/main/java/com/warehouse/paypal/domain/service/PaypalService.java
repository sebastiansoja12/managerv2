package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.Payment;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentResponse;

public interface PaypalService {
    PaymentInformation payment(Payment payment);

    String update(PaymentInformation paymentInformation);
}
