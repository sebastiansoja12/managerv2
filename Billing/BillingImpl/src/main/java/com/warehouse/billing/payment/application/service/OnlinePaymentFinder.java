package com.warehouse.billing.payment.application.service;

import com.warehouse.billing.payment.application.port.primary.command.ConfirmGatewayRefundCommand;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.secondary.LateChargeRefundRepository;
import com.warehouse.billing.payment.application.port.secondary.PaymentRepository;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.commonassets.identificator.OperatorId;

import java.util.Optional;

public class OnlinePaymentFinder {

    private final PaymentRepository paymentRepository;

    private final LateChargeRefundRepository lateChargeRefundRepository;

    public OnlinePaymentFinder(final PaymentRepository paymentRepository,
                               final LateChargeRefundRepository lateChargeRefundRepository) {
        this.paymentRepository = paymentRepository;
        this.lateChargeRefundRepository = lateChargeRefundRepository;
    }

    public Optional<OperatorId> findOperator(final ConfirmOnlinePaymentCommand command) {
        return Optional.ofNullable(command.paymentId())
                .flatMap(this.paymentRepository::findOperatorByPaymentId)
                .or(() -> Optional.ofNullable(command.gatewayReference())
                        .flatMap(this.paymentRepository::findOperatorByGatewayReference));
    }

    public Optional<Payment> find(final ConfirmOnlinePaymentCommand command) {
        return Optional.ofNullable(command.paymentId())
                .flatMap(this.paymentRepository::findById)
                .or(() -> Optional.ofNullable(command.gatewayReference())
                        .flatMap(this.paymentRepository::findByGatewayReference));
    }

    public Optional<OperatorId> findOperator(final ConfirmGatewayRefundCommand command) {
        return this.paymentRepository.findOperatorByGatewayPaymentIntent(command.gatewayPaymentIntent())
                .or(() -> this.lateChargeRefundRepository.findOperatorByGatewayPaymentIntent(
                        command.gatewayPaymentIntent()));
    }

    public Optional<Payment> find(final ConfirmGatewayRefundCommand command) {
        return this.paymentRepository.findByGatewayPaymentIntent(command.gatewayPaymentIntent());
    }

    public boolean isLateChargeRefund(final ConfirmGatewayRefundCommand command) {
        return this.lateChargeRefundRepository.existsByGatewayPaymentIntent(command.gatewayPaymentIntent());
    }
}
