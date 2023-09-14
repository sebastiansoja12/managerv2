package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;

public interface PaypalService {
    PaymentInformation payment(PaymentRequest payment);

    String update(PaymentInformation paymentInformation);
}
