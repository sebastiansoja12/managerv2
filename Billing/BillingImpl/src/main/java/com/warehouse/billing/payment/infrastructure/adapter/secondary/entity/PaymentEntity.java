package com.warehouse.billing.payment.infrastructure.adapter.secondary.entity;

import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.commonassets.model.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@NoArgsConstructor
@Entity(name = "billing.PaymentEntity")
@Table(name = "payments")
public class PaymentEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private PaymentId paymentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "price_quote_id", nullable = false, unique = true))
    private PriceQuoteId priceQuoteId;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount",
                    column = @Column(name = "amount", nullable = false, precision = 12, scale = 2)),
            @AttributeOverride(name = "currency", column = @Column(name = "currency", nullable = false, length = 3))
    })
    private Money amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel", length = 20)
    private PaymentChannel channel;

    @Enumerated(EnumType.STRING)
    @Column(name = "method", length = 20)
    private PaymentMethod method;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PaymentStatus status;

    @Column(name = "gateway_reference")
    private String gatewayReference;

    @Column(name = "checkout_url", length = 2048)
    private String checkoutUrl;

    @Column(name = "checkout_expires_at")
    private Instant checkoutExpiresAt;

    @Column(name = "checkout_attempt", nullable = false)
    private int checkoutAttempt;

    @Column(name = "deadline", nullable = false)
    private Instant deadline;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "paid_at")
    private Instant paidAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "recorded_by"))
    private UserId recordedBy;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "department_id"))
    private DepartmentId department;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "buyer_tax_id", length = 32))
    private TaxId buyerTaxId;

    @Column(name = "gateway_payment_intent")
    private String gatewayPaymentIntent;

    @Column(name = "refund_requested_at")
    private Instant refundRequestedAt;

    @Column(name = "refunded_at")
    private Instant refundedAt;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "refunded_by"))
    private UserId refundedBy;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    public PaymentEntity(final PaymentId paymentId, final PriceQuoteId priceQuoteId, final Money amount,
                         final PaymentChannel channel, final PaymentMethod method, final PaymentStatus status,
                         final String gatewayReference, final String checkoutUrl,
                         final Instant checkoutExpiresAt, final int checkoutAttempt, final Instant deadline,
                         final Instant createdAt,
                         final Instant paidAt, final UserId recordedBy, final DepartmentId department,
                         final TaxId buyerTaxId, final String gatewayPaymentIntent,
                         final Instant refundRequestedAt, final Instant refundedAt, final UserId refundedBy,
                         final Long version) {
        this.paymentId = paymentId;
        this.priceQuoteId = priceQuoteId;
        this.amount = amount;
        this.channel = channel;
        this.method = method;
        this.status = status;
        this.gatewayReference = gatewayReference;
        this.checkoutUrl = checkoutUrl;
        this.checkoutExpiresAt = checkoutExpiresAt;
        this.checkoutAttempt = checkoutAttempt;
        this.deadline = deadline;
        this.createdAt = createdAt;
        this.paidAt = paidAt;
        this.recordedBy = recordedBy;
        this.department = department;
        this.buyerTaxId = buyerTaxId;
        this.gatewayPaymentIntent = gatewayPaymentIntent;
        this.refundRequestedAt = refundRequestedAt;
        this.refundedAt = refundedAt;
        this.refundedBy = refundedBy;
        this.version = version;
    }
}
