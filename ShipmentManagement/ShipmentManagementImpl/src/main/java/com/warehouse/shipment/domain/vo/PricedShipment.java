package com.warehouse.shipment.domain.vo;

import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

public record PricedShipment(PriceQuoteId quoteId, Money gross) {
}
