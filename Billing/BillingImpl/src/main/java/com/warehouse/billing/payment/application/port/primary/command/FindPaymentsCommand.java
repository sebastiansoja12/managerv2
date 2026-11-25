package com.warehouse.billing.payment.application.port.primary.command;

import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.commonassets.identificator.PriceQuoteId;

public record FindPaymentsCommand(PaymentStatus status,
                                  PriceQuoteId priceQuoteId,
                                  int page,
                                  int size) {

    public static final int MAX_PAGE_SIZE = 100;

    public FindPaymentsCommand {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new PaymentException(PaymentException.Reason.INVALID_REQUEST,
                    "Page must be at least 0 and size between 1 and " + MAX_PAGE_SIZE);
        }
    }
}
