package com.warehouse.paypal.domain.port.secondary;

import com.paypal.api.payments.Payment;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;

public interface PaypalServicePort {

    Payment update(String paymentId, String payerId);

    PaypalResponse payment(PaypalRequest paypalRequest);
}
