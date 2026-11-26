package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.application.port.secondary.PaymentGatewayServicePort;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.service.LateChargeRefundFactory;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class LateChargeRefundService {

    private final LateChargeRefundRepository lateChargeRefundRepository;

    private final PaymentGatewayServicePort paymentGatewayServicePort;

    private final LateChargeRefundFactory lateChargeRefundFactory;

    public LateChargeRefundService(final LateChargeRefundRepository lateChargeRefundRepository,
                                   final PaymentGatewayServicePort paymentGatewayServicePort,
                                   final LateChargeRefundFactory lateChargeRefundFactory) {
        this.lateChargeRefundRepository = lateChargeRefundRepository;
        this.paymentGatewayServicePort = paymentGatewayServicePort;
        this.lateChargeRefundFactory = lateChargeRefundFactory;
    }

    public void refund(final Payment payment, final String gatewayReference, final String gatewayPaymentIntent) {
        final String chargedSession = Optional.ofNullable(gatewayReference)
                .or(() -> payment.checkoutSession().map(CheckoutSession::gatewayReference))
                .orElseThrow(() -> new PaymentException(PaymentException.Reason.ILLEGAL_STATE,
                        "Late charge of payment in status " + payment.status()
                                + " cannot be refunded without a checkout session"));
        if (this.lateChargeRefundRepository.exists(chargedSession)) {
            return;
        }
        this.paymentGatewayServicePort.refundLateCharge(chargedSession, payment.id());
        this.lateChargeRefundRepository.save(this.lateChargeRefundFactory.create(payment, chargedSession,
                gatewayPaymentIntent));
        log.info("Late charge of payment {} in status {} was refunded", payment.id().value(), payment.status());
    }
}
