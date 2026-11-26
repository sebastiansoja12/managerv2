package com.warehouse.billing.pricing.application.port.secondary;

import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.util.Optional;

public interface PriceQuoteRepository {

    void create(final PriceQuote quote);

    Optional<PriceQuote> findById(final PriceQuoteId quoteId);
}
