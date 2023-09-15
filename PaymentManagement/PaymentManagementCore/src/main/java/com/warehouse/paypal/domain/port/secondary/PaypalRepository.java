package com.warehouse.paypal.domain.port.secondary;

import com.warehouse.paypal.domain.model.PaymentInformation;
import com.warehouse.paypal.domain.model.PaymentUpdateRequest;

public interface PaypalRepository {

    void savePayment(PaymentInformation payment);

    void updatePayment(PaymentUpdateRequest request);

    PaymentInformation findByPaymentId(Long paypalId);
}
