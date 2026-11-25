package com.warehouse.billing.payment.application.port.secondary;

import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.commonassets.identificator.PaymentId;

public interface PaymentGatewayServicePort {

    CheckoutSession openCheckout(final Payment payment);

    CheckoutClosure expireCheckout(final String gatewayReference);

    GatewayRefund refund(final Payment payment);

    void refundLateCharge(final String gatewayReference, final PaymentId paymentId);
}
