package com.warehouse.billing.api;

import com.warehouse.billing.api.dto.PriceQuoteDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;

public interface PriceQuoteApiService {

    PriceQuoteDto quote(final PriceQuoteRequestDto request);
}
