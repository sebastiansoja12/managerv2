package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.billing.payment.domain.vo.PaymentQuoteReference;
import com.warehouse.commonassets.identificator.TrackingNumber;

import java.util.Optional;

public interface PaymentQuoteLookupServicePort {

    Optional<PaymentQuoteReference> find(final TrackingNumber trackingNumber);
}
