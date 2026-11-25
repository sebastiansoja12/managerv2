package com.warehouse.billing.payment.domain.vo;

import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;

public record GatewayRefund(RefundOutcome outcome, String gatewayPaymentIntent) {
}
