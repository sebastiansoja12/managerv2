package com.warehouse.billing.invoice.application.port.secondary;

import com.warehouse.billing.invoice.domain.vo.InvoiceBuyer;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;

public interface InvoiceBuyerServicePort {

    InvoiceBuyer findBuyer(final PriceQuoteId priceQuoteId, final TaxId buyerTaxId);
}
