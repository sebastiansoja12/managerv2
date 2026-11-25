package com.warehouse.billing.payment.infrastructure.adapter.primary.api;

import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.commonassets.enumeration.Currency;

import java.math.BigDecimal;
import java.time.Instant;

public record PaymentApiResponse(PaymentIdDto paymentId,
                                 PriceQuoteIdDto priceQuoteId,
                                 BigDecimal amount,
                                 Currency currency,
                                 PaymentChannel channel,
                                 PaymentMethod method,
                                 PaymentStatus status,
                                 Instant deadline,
                                 Instant createdAt,
                                 Instant paidAt,
                                 UserIdDto recordedBy,
                                 DepartmentIdDto departmentId,
                                 String buyerTaxId,
                                 Instant refundRequestedAt,
                                 Instant refundedAt,
                                 UserIdDto refundedBy) {
}
