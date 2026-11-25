package com.warehouse.billing.payment.application.port.primary;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.command.OpenPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.RecordCounterPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.result.CheckoutView;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.application.port.secondary.PaymentActorServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentOperatorContextServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentQuoteLookupServicePort;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.application.service.OnlineCheckoutService;
import com.warehouse.billing.payment.application.service.OnlinePaymentConfirmationService;
import com.warehouse.billing.payment.application.service.OnlinePaymentFinder;
import com.warehouse.billing.payment.application.service.PaymentExpiryService;
import com.warehouse.billing.payment.application.service.PaymentOpeningService;
import com.warehouse.billing.payment.application.service.PaymentRefundService;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.event.PaymentPaid;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.exception.PaymentNotReadyException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.PaymentActor;
import com.warehouse.billing.payment.domain.vo.PaymentQuoteReference;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.BillingCustomerId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.Optional;

public class PaymentPortImpl implements PaymentPort {

    private final PaymentRepository paymentRepository;

    private final PaymentOpeningService paymentOpeningService;

    private final PaymentActorServicePort paymentActorServicePort;

    private final DomainEventPublisher domainEventPublisher;

    private final Clock clock;

    private final OnlineCheckoutService onlineCheckoutService;

    private final OnlinePaymentConfirmationService onlinePaymentConfirmationService;

    private final PaymentQuoteLookupServicePort paymentQuoteLookupServicePort;

    private final PaymentOperatorContextServicePort paymentOperatorContextServicePort;

    private final OnlinePaymentFinder onlinePaymentFinder;

    private final PaymentExpiryService paymentExpiryService;

    private final PaymentRefundService paymentRefundService;

    public PaymentPortImpl(final PaymentRepository paymentRepository,
                           final PaymentOpeningService paymentOpeningService,
                           final PaymentActorServicePort paymentActorServicePort,
                           final DomainEventPublisher domainEventPublisher,
                           final Clock clock,
                           final OnlineCheckoutService onlineCheckoutService,
                           final OnlinePaymentConfirmationService onlinePaymentConfirmationService,
                           final PaymentQuoteLookupServicePort paymentQuoteLookupServicePort,
                           final PaymentOperatorContextServicePort paymentOperatorContextServicePort,
                           final OnlinePaymentFinder onlinePaymentFinder,
                           final PaymentExpiryService paymentExpiryService,
                           final PaymentRefundService paymentRefundService) {
        this.paymentRepository = paymentRepository;
        this.paymentOpeningService = paymentOpeningService;
        this.paymentActorServicePort = paymentActorServicePort;
        this.domainEventPublisher = domainEventPublisher;
        this.clock = clock;
        this.onlineCheckoutService = onlineCheckoutService;
        this.onlinePaymentConfirmationService = onlinePaymentConfirmationService;
        this.paymentQuoteLookupServicePort = paymentQuoteLookupServicePort;
        this.paymentOperatorContextServicePort = paymentOperatorContextServicePort;
        this.onlinePaymentFinder = onlinePaymentFinder;
        this.paymentExpiryService = paymentExpiryService;
        this.paymentRefundService = paymentRefundService;
    }

    @Override
    @Transactional
    public void open(final OpenPaymentCommand command) {
        if (!command.paymentRequired()
                || this.paymentRepository.findByPriceQuote(command.priceQuoteId()).isPresent()) {
            return;
        }
        this.paymentOpeningService.open(command.priceQuoteId()).ifPresent(this.paymentRepository::save);
    }

    @Override
    @Transactional
    public Payment recordCounterPayment(final RecordCounterPaymentCommand command) {
        final Payment payment = this.paymentRepository.findByPriceQuote(command.priceQuoteId())
                .orElseGet(() -> this.paymentOpeningService.openForCounter(command.priceQuoteId()));
        final PaymentStatus previousStatus = payment.status();
        final PaymentActor actor = this.paymentActorServicePort.currentActor();
        payment.markPaidAtCounter(command.method(), actor.userId(), actor.departmentId(), command.buyerTaxId(),
                this.clock.instant());
        if (previousStatus == payment.status()) {
            return payment;
        }
        this.paymentRepository.save(payment);
        this.domainEventPublisher.publish(new PaymentPaid(payment.snapshot(), this.clock.instant()));
        return payment;
    }

    @Override
    @Transactional
    public CheckoutView openOnlineCheckout(final TrackingNumber trackingNumber,
                                           final BillingCustomerId billingCustomerId) {
        final PaymentQuoteReference reference = this.paymentQuoteLookupServicePort.find(trackingNumber)
                .filter(candidate -> candidate.createdBy(billingCustomerId))
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND,
                        "Shipment was not found"));
        return this.paymentOperatorContextServicePort.runAs(reference.operatorId(),
                () -> this.onlineCheckoutService.open(reference.payableQuoteId()));
    }

    @Override
    @Transactional(noRollbackFor = PaymentException.class)
    public Payment confirmOnlinePayment(final ConfirmOnlinePaymentCommand command) {
        final OperatorId operatorId = this.onlinePaymentFinder.findOperator(command)
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.NOT_FOUND,
                        "Payment of the checkout session was not found"));
        return this.paymentOperatorContextServicePort.runAs(operatorId,
                () -> this.onlinePaymentConfirmationService.confirm(command));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findByPriceQuote(final PriceQuoteId priceQuoteId) {
        return this.paymentRepository.findByPriceQuote(priceQuoteId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Payment> findById(final PaymentId paymentId) {
        return this.paymentRepository.findById(paymentId);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentPage find(final FindPaymentsCommand command) {
        return this.paymentRepository.find(command);
    }

    @Override
    public void expireOverdue(final Instant now) {
        this.paymentExpiryService.expireOverdue(now);
    }

    @Override
    @Transactional
    public void handleShipmentCanceled(final PriceQuoteId priceQuoteId, final ShipmentStatus previousStatus) {
        this.paymentRefundService.cancelForShipment(priceQuoteId, previousStatus);
    }

    @Override
    @Transactional
    public Payment requestManualRefund(final PaymentId paymentId) {
        return this.paymentRefundService.requestRefund(paymentId);
    }

    @Override
    @Transactional
    public Payment confirmCounterRefund(final PaymentId paymentId) {
        return this.paymentRefundService.confirmCounterRefund(paymentId,
                this.paymentActorServicePort.currentActor().userId());
    }

    @Override
    @Transactional(noRollbackFor = PaymentException.class)
    public void confirmGatewayRefund(final ConfirmGatewayRefundCommand command) {
        final OperatorId operatorId = this.onlinePaymentFinder.findOperator(command)
                .orElseThrow(() -> new PaymentNotReadyException("Payment of the refunded charge is not known yet"));
        this.paymentOperatorContextServicePort.runAs(operatorId,
                () -> this.onlinePaymentConfirmationService.confirmRefund(command));
    }
}
