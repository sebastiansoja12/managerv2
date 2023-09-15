package com.warehouse.paypal.domain.service;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentUpdateRequest;
import com.warehouse.paypal.domain.model.PaymentUpdateResponse;

public interface PaypalService {
    PaymentInformation payment(PaymentRequest payment);

    PaymentUpdateResponse update(PaymentUpdateRequest paymentUpdateRequest);
}
