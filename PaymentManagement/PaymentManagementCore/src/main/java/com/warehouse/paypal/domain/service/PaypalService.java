package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.Payee;
import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;

public interface PaypalService {
    PaymentInformation payment(PaymentRequest payment, Payee payee);

    String update(PaymentInformation paymentInformation);
}
