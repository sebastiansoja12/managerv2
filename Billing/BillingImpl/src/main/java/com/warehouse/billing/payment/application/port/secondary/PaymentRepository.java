package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository {

    void save(final Payment payment);

    Optional<Payment> findById(final PaymentId paymentId);

    Optional<Payment> findByPriceQuote(final PriceQuoteId priceQuoteId);

    Optional<Payment> findByGatewayReference(final String gatewayReference);

    Optional<Payment> findByGatewayPaymentIntent(final String gatewayPaymentIntent);

    Optional<OperatorId> findOperatorByGatewayReference(final String gatewayReference);

    Optional<OperatorId> findOperatorByGatewayPaymentIntent(final String gatewayPaymentIntent);

    Optional<OperatorId> findOperatorByPaymentId(final PaymentId paymentId);

    List<Payment> findOverduePending(final Instant now, final int limit);

    PaymentPage find(final FindPaymentsCommand command);
}
