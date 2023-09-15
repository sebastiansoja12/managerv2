package com.warehouse.paypal.domain.port.secondary;

import com.warehouse.paypal.domain.model.PaymentUpdateRequest;
import com.warehouse.paypal.domain.model.PaypalRequest;
import com.warehouse.paypal.domain.model.PaypalResponse;
import com.warehouse.paypal.domain.model.PaypalUpdateResponse;

public interface PaypalServicePort {

    PaypalUpdateResponse update(PaymentUpdateRequest updateRequest);

    PaypalResponse payment(PaypalRequest paypalRequest);
}
