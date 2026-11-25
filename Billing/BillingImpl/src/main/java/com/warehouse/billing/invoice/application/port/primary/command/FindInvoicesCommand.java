package com.warehouse.billing.invoice.application.port.primary.command;

import com.warehouse.billing.invoice.domain.exception.InvoiceException;
import com.warehouse.commonassets.identificator.PriceQuoteId;

public record FindInvoicesCommand(PriceQuoteId priceQuoteId,
                                  int page,
                                  int size) {

    public static final int MAX_PAGE_SIZE = 100;

    public FindInvoicesCommand {
        if (page < 0 || size < 1 || size > MAX_PAGE_SIZE) {
            throw new InvoiceException(InvoiceException.Reason.INVALID_REQUEST,
                    "Page must be at least 0 and size between 1 and " + MAX_PAGE_SIZE);
        }
    }
}
