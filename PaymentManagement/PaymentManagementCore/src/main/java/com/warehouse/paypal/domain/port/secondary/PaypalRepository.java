package com.warehouse.paypal.domain.port.secondary;

import com.warehouse.paypal.domain.model.PaymentInformation;

public interface PaypalRepository {

    void savePayment(PaymentInformation payment);

    void updatePayment(Long paymentId);

    PaymentInformation findByPaymentId(Long paypalId);
}
