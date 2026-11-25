package com.warehouse.billing.payment.domain.vo;

import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.time.Instant;

public record PaymentSnapshot(PaymentId paymentId,
                              PriceQuoteId priceQuoteId,
                              Money amount,
                              PaymentChannel channel,
                              PaymentMethod method,
                              PaymentStatus status,
                              Instant paidAt,
                              Instant refundedAt) {
}
