package com.warehouse.billing.pricing.application.port.primary;

import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.application.port.primary.command.ReplacePriceListCommand;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;

import java.util.Optional;

public interface PricingPort {

    PriceQuote quote(final QuoteShipmentCommand command);

    PriceQuote preview(final QuoteShipmentCommand command);

    Optional<PriceQuote> findQuote(final PriceQuoteId quoteId);

    PriceList getPriceList();

    PriceList replacePriceList(final ReplacePriceListCommand command);

    void createDefaultPriceList(final OperatorId operatorId);

    PriceList findTemplate();
}
