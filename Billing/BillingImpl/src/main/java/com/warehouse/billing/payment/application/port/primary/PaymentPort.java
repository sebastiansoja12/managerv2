package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.command.OpenPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.RecordCounterPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.result.CheckoutView;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TrackingNumber;

import java.time.Instant;
import java.util.Optional;

public interface PaymentPort {

    void open(final OpenPaymentCommand command);

    Payment recordCounterPayment(final RecordCounterPaymentCommand command);

    CheckoutView openOnlineCheckout(final TrackingNumber trackingNumber, final BillingCustomerId billingCustomerId);

    Payment confirmOnlinePayment(final ConfirmOnlinePaymentCommand command);

    Optional<Payment> findByPriceQuote(final PriceQuoteId priceQuoteId);

    Optional<Payment> findById(final PaymentId paymentId);

    PaymentPage find(final FindPaymentsCommand command);

    void expireOverdue(final Instant now);

    void handleShipmentCanceled(final PriceQuoteId priceQuoteId, final ShipmentStatus previousStatus);

    Payment requestManualRefund(final PaymentId paymentId);

    Payment confirmCounterRefund(final PaymentId paymentId);

    void confirmGatewayRefund(final ConfirmGatewayRefundCommand command);
}
