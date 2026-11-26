package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class InMemoryPaymentRepository implements PaymentRepository {

    private final Map<PaymentId, Payment> payments = new LinkedHashMap<>();

    private int saves;

    @Override
    public void save(final Payment payment) {
        this.payments.put(payment.id(), payment);
        this.saves++;
    }

    @Override
    public Optional<Payment> findById(final PaymentId paymentId) {
        return Optional.ofNullable(this.payments.get(paymentId));
    }

    @Override
    public Optional<Payment> findByPriceQuote(final PriceQuoteId priceQuoteId) {
        return this.payments.values().stream().filter(payment -> payment.priceQuoteId().equals(priceQuoteId))
                .findFirst();
    }

    @Override
    public Optional<Payment> findByGatewayReference(final String gatewayReference) {
        return this.payments.values().stream()
                .filter(payment -> payment.checkoutSession().map(CheckoutSession::gatewayReference)
                        .filter(gatewayReference::equals).isPresent())
                .findFirst();
    }

    @Override
    public Optional<Payment> findByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return this.payments.values().stream()
                .filter(payment -> gatewayPaymentIntent.equals(payment.gatewayPaymentIntent()))
                .findFirst();
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent) {
        return findByGatewayPaymentIntent(gatewayPaymentIntent).map(payment -> OperatorId.of(10L));
    }

    @Override
    public Optional<OperatorId> findOperatorByGatewayReference(final String gatewayReference) {
        return findByGatewayReference(gatewayReference).map(payment -> OperatorId.of(10L));
    }

    @Override
    public Optional<OperatorId> findOperatorByPaymentId(final PaymentId paymentId) {
        return findById(paymentId).map(payment -> OperatorId.of(10L));
    }

    @Override
    public List<Payment> findOverduePending(final Instant now, final int limit) {
        return this.payments.values().stream()
                .filter(payment -> PaymentStatus.PENDING.equals(payment.status()))
                .filter(payment -> payment.deadline().isBefore(now))
                .sorted(Comparator.comparing(Payment::deadline))
                .limit(limit)
                .toList();
    }

    @Override
    public PaymentPage find(final FindPaymentsCommand command) {
        final List<Payment> all = new ArrayList<>(this.payments.values());
        return new PaymentPage(all, command.page(), command.size(), all.size());
    }

    List<Payment> all() {
        return List.copyOf(this.payments.values());
    }

    int saves() {
        return this.saves;
    }
}
