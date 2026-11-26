package com.warehouse.billing.payment.application.listener;

import com.warehouse.billing.api.event.PaymentExpiredIntegrationEvent;
import com.warehouse.billing.api.event.PaymentRefundedIntegrationEvent;
import com.warehouse.billing.api.event.PriceQuotePaidIntegrationEvent;
import com.warehouse.billing.api.event.snapshot.PriceQuotePaidData;
import com.warehouse.billing.payment.domain.event.PaymentExpired;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.event.PaymentRefunded;
import com.warehouse.billing.payment.domain.vo.PaymentSnapshot;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class PaymentIntegrationEventListener {

    @IntegrationEventTranslation
    public PriceQuotePaidIntegrationEvent translate(final PaymentPaid event) {
        final PaymentSnapshot snapshot = event.getSnapshot();
        return new PriceQuotePaidIntegrationEvent(new PriceQuotePaidData(snapshot.priceQuoteId(),
                snapshot.paymentId(), snapshot.paidAt()));
    }

    @IntegrationEventTranslation
    public PaymentExpiredIntegrationEvent translate(final PaymentExpired event) {
        final PaymentSnapshot snapshot = event.getSnapshot();
        return new PaymentExpiredIntegrationEvent(snapshot.priceQuoteId(), snapshot.paymentId(),
                event.getTimestamp());
    }

    @IntegrationEventTranslation
    public PaymentRefundedIntegrationEvent translate(final PaymentRefunded event) {
        final PaymentSnapshot snapshot = event.getSnapshot();
        return new PaymentRefundedIntegrationEvent(snapshot.priceQuoteId(), snapshot.paymentId(), snapshot.amount(),
                snapshot.refundedAt());
    }
}
