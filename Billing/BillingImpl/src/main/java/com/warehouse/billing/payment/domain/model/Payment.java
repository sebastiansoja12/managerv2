package com.warehouse.billing.payment.domain.model;

import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.billing.payment.domain.vo.PaymentSnapshot;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.Money;

import java.time.Instant;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class Payment {

    private static final Set<ShipmentStatus> DRAFT_SHIPMENT_STATUSES = EnumSet.of(ShipmentStatus.PLANNED,
            ShipmentStatus.CREATED, ShipmentStatus.PREPARED);

    private final PaymentId id;

    private final PriceQuoteId priceQuoteId;

    private final Money amount;

    private PaymentChannel channel;

    private PaymentMethod method;

    private PaymentStatus status;

    private CheckoutSession checkoutSession;

    private int checkoutAttempt;

    private final Instant deadline;

    private final Instant createdAt;

    private Instant paidAt;

    private UserId recordedBy;

    private DepartmentId department;

    private TaxId buyerTaxId;

    private String gatewayPaymentIntent;

    private Instant refundRequestedAt;

    private Instant refundedAt;

    private UserId refundedBy;

    private final Long version;

    public Payment(final PaymentId id, final PriceQuoteId priceQuoteId, final Money amount,
                   final PaymentChannel channel, final PaymentMethod method, final PaymentStatus status,
                   final CheckoutSession checkoutSession, final int checkoutAttempt,
                   final Instant deadline, final Instant createdAt,
                   final Instant paidAt, final UserId recordedBy, final DepartmentId department,
                   final TaxId buyerTaxId, final String gatewayPaymentIntent, final Instant refundRequestedAt,
                   final Instant refundedAt, final UserId refundedBy, final Long version) {
        this.id = id;
        this.priceQuoteId = priceQuoteId;
        this.amount = amount;
        this.channel = channel;
        this.method = method;
        this.status = status;
        this.checkoutSession = checkoutSession;
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

    public void markPaid(final PaymentMethod method, final UserId recordedBy, final DepartmentId department,
                         final Instant paidAt) {
        pay(method, recordedBy, department, null, paidAt);
    }

    public void markPaidAtCounter(final PaymentMethod method, final UserId recordedBy, final DepartmentId department,
                                  final TaxId buyerTaxId, final Instant paidAt) {
        if (!PaymentChannel.COUNTER.equals(method.channel())) {
            throw new PaymentException(PaymentException.Reason.INVALID_REQUEST,
                    "Counter payment accepts only cash or card");
        }
        pay(method, recordedBy, department, buyerTaxId, paidAt);
    }

    public void markPaidOnline(final Instant paidAt, final String gatewayReference,
                               final String gatewayPaymentIntent, final Runnable lateChargeRefund) {
        if (PaymentStatus.PENDING.equals(this.status)) {
            pay(PaymentMethod.STRIPE, null, null, null, paidAt);
            this.gatewayPaymentIntent = gatewayPaymentIntent;
            if (gatewayReference != null && checkoutSession().map(CheckoutSession::gatewayReference)
                    .filter(gatewayReference::equals).isEmpty()) {
                this.checkoutSession = new CheckoutSession(gatewayReference, null, paidAt);
            }
            return;
        }
        if (PaymentMethod.STRIPE.equals(this.method) && (gatewayReference == null
                || checkoutSession().map(CheckoutSession::gatewayReference).filter(gatewayReference::equals)
                .isPresent())) {
            return;
        }
        lateChargeRefund.run();
    }

    public void cancelForShipment(final ShipmentStatus previousStatus, final Instant now,
                                  final Supplier<GatewayRefund> gatewayRefund, final Runnable checkoutExpiry) {
        if (PaymentStatus.PENDING.equals(this.status)) {
            this.status = PaymentStatus.CANCELED;
            checkoutExpiry.run();
            return;
        }
        if (PaymentStatus.PAID.equals(this.status) && DRAFT_SHIPMENT_STATUSES.contains(previousStatus)) {
            requestRefund(now, gatewayRefund);
        }
    }

    public void requestRefund(final Instant now, final Supplier<GatewayRefund> gatewayRefund) {
        if (PaymentStatus.REFUND_PENDING.equals(this.status) || PaymentStatus.REFUNDED.equals(this.status)) {
            return;
        }
        if (!PaymentStatus.PAID.equals(this.status)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Payment cannot be refunded in status " + this.status);
        }
        if (PaymentChannel.ONLINE.equals(this.channel)) {
            final GatewayRefund refund = gatewayRefund.get();
            this.gatewayPaymentIntent = refund.gatewayPaymentIntent();
            this.status = PaymentStatus.REFUND_PENDING;
            this.refundRequestedAt = now;
            if (RefundOutcome.COMPLETED.equals(refund.outcome())) {
                completeRefund(now);
            }
            return;
        }
        this.status = PaymentStatus.REFUND_PENDING;
        this.refundRequestedAt = now;
    }

    public void markRefunded(final Instant now) {
        if (PaymentStatus.REFUNDED.equals(this.status)) {
            return;
        }
        if (PaymentStatus.PAID.equals(this.status)) {
            throw new PaymentNotReadyException("Refund of the payment is not recorded yet");
        }
        if (!PaymentStatus.REFUND_PENDING.equals(this.status)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Refund cannot be confirmed for payment in status " + this.status);
        }
        completeRefund(now);
    }

    public void markRefundedAtCounter(final UserId refundedBy, final Instant now) {
        if (!PaymentChannel.COUNTER.equals(this.channel)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Only counter payments are refunded at the counter");
        }
        if (PaymentStatus.REFUNDED.equals(this.status)) {
            return;
        }
        if (!PaymentStatus.REFUND_PENDING.equals(this.status)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Counter refund cannot be confirmed for payment in status " + this.status);
        }
        completeRefund(now);
        this.refundedBy = refundedBy;
    }

    private void completeRefund(final Instant now) {
        this.status = PaymentStatus.REFUNDED;
        this.refundedAt = now;
    }

    public void expire(final Instant now) {
        if (!PaymentStatus.PENDING.equals(this.status) || !now.isAfter(this.deadline)) {
            return;
        }
        this.status = PaymentStatus.EXPIRED;
    }

    public void attachCheckout(final CheckoutSession session, final Instant now) {
        if (session.equals(this.checkoutSession)) {
            return;
        }
        if (!acceptsOnlineCheckout(now)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Checkout cannot be opened for payment in status " + this.status + " or after its deadline");
        }
        this.checkoutSession = session;
        this.checkoutAttempt = nextCheckoutAttempt();
        this.channel = PaymentChannel.ONLINE;
    }

    public int nextCheckoutAttempt() {
        return this.checkoutAttempt + 1;
    }

    public boolean acceptsOnlineCheckout(final Instant now) {
        return PaymentStatus.PENDING.equals(this.status) && now.isBefore(this.deadline);
    }

    public Optional<CheckoutSession> openCheckout(final Instant now) {
        return Optional.ofNullable(this.checkoutSession)
                .filter(session -> acceptsOnlineCheckout(now) && session.isOpenAt(now));
    }

    private void pay(final PaymentMethod method, final UserId recordedBy, final DepartmentId department,
                     final TaxId buyerTaxId, final Instant paidAt) {
        if (PaymentStatus.PAID.equals(this.status) && method.equals(this.method)) {
            return;
        }
        if (!PaymentStatus.PENDING.equals(this.status)) {
            throw new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                    "Payment cannot be marked as paid in status " + this.status);
        }
        this.status = PaymentStatus.PAID;
        this.method = method;
        this.channel = method.channel();
        this.recordedBy = recordedBy;
        this.department = department;
        this.buyerTaxId = buyerTaxId;
        this.paidAt = paidAt;
    }

    public PaymentSnapshot snapshot() {
        return new PaymentSnapshot(id, priceQuoteId, amount, channel, method, status, paidAt, refundedAt);
    }

    public PaymentId id() {
        return id;
    }

    public PriceQuoteId priceQuoteId() {
        return priceQuoteId;
    }

    public Money amount() {
        return amount;
    }

    public PaymentChannel channel() {
        return channel;
    }

    public PaymentMethod method() {
        return method;
    }

    public PaymentStatus status() {
        return status;
    }

    public Optional<CheckoutSession> checkoutSession() {
        return Optional.ofNullable(checkoutSession);
    }

    public int checkoutAttempt() {
        return checkoutAttempt;
    }

    public Instant deadline() {
        return deadline;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant paidAt() {
        return paidAt;
    }

    public UserId recordedBy() {
        return recordedBy;
    }

    public DepartmentId department() {
        return department;
    }

    public TaxId buyerTaxId() {
        return buyerTaxId;
    }

    public String gatewayPaymentIntent() {
        return gatewayPaymentIntent;
    }

    public Instant refundRequestedAt() {
        return refundRequestedAt;
    }

    public Instant refundedAt() {
        return refundedAt;
    }

    public UserId refundedBy() {
        return refundedBy;
    }

    public Long version() {
        return version;
    }
}
