package com.warehouse.billing.payment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.commonassets.model.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity(name = "billing.LateChargeRefundEntity")
@Table(name = "payment_late_refunds")
public class LateChargeRefundEntity extends BelongsToOperator {

    @Id
    @Column(name = "session_id")
    private String gatewayReference;

    @Column(name = "gateway_payment_intent", unique = true)
    private String gatewayPaymentIntent;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "payment_id", nullable = false))
    private PaymentId paymentId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount",
                    column = @Column(name = "amount", nullable = false, precision = 12, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money amount;

    @Column(name = "refunded_at", nullable = false)
    private Instant refundedAt;

    public LateChargeRefundEntity(final String gatewayReference, final String gatewayPaymentIntent,
                                  final PaymentId paymentId, final Money amount, final Instant refundedAt) {
        this.gatewayReference = gatewayReference;
        this.gatewayPaymentIntent = gatewayPaymentIntent;
        this.paymentId = paymentId;
        this.amount = amount;
        this.refundedAt = refundedAt;
    }
}
