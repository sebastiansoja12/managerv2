package com.warehouse.billing.payment.infrastructure.adapter.secondary.mapper;

import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.infrastructure.adapter.secondary.entity.PaymentEntity;
import com.warehouse.commonassets.model.Money;

public class PaymentPersistenceMapper {

    public PaymentEntity toEntity(final Payment payment) {
        final CheckoutSession checkoutSession = payment.checkoutSession().orElse(null);
        return new PaymentEntity(payment.id(), payment.priceQuoteId(),
                Money.from(payment.amount().getAmount(), payment.amount().getCurrency()), payment.channel(),
                payment.method(), payment.status(),
                checkoutSession == null ? null : checkoutSession.gatewayReference(),
                checkoutSession == null ? null : checkoutSession.url(),
                checkoutSession == null ? null : checkoutSession.expiresAt(), payment.checkoutAttempt(),
                payment.deadline(), payment.createdAt(), payment.paidAt(), payment.recordedBy(),
                payment.department(), payment.buyerTaxId(), payment.gatewayPaymentIntent(),
                payment.refundRequestedAt(), payment.refundedAt(), payment.refundedBy(), payment.version());
    }

    public Payment toDomain(final PaymentEntity entity) {
        return new Payment(entity.getPaymentId(), entity.getPriceQuoteId(),
                Money.from(entity.getAmount().getAmount(), entity.getAmount().getCurrency()), entity.getChannel(),
                entity.getMethod(), entity.getStatus(),
                entity.getGatewayReference() == null ? null : new CheckoutSession(entity.getGatewayReference(),
                        entity.getCheckoutUrl(), entity.getCheckoutExpiresAt()),
                entity.getCheckoutAttempt(),
                entity.getDeadline(), entity.getCreatedAt(), entity.getPaidAt(), entity.getRecordedBy(),
                entity.getDepartment(), entity.getBuyerTaxId(), entity.getGatewayPaymentIntent(),
                entity.getRefundRequestedAt(), entity.getRefundedAt(), entity.getRefundedBy(), entity.getVersion());
    }
}
