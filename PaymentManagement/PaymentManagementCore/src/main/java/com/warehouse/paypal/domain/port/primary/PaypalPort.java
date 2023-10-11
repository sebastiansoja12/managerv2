package com.warehouse.paypal.domain.port.primary;

import com.warehouse.paypal.domain.model.PaymentRequest;
import com.warehouse.paypal.domain.model.PaymentResponse;
import com.warehouse.paypal.domain.model.PaymentUpdateRequest;
import com.warehouse.paypal.domain.model.PaymentUpdateResponse;

public interface PaypalPort {

    PaymentResponse payment(PaymentRequest request);

    PaymentUpdateResponse update(PaymentUpdateRequest paymentUpdateRequest);
}
