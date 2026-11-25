package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.billing.api.PriceQuoteApiService;
import com.warehouse.billing.api.dto.PriceQuoteDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;
import com.warehouse.billing.api.exception.PriceListNotFoundApiException;
import com.warehouse.billing.api.exception.ShipmentNotPriceableApiException;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper.PricingWebMapper;

public class PriceQuoteApiServiceAdapter implements PriceQuoteApiService {

    private final PricingPort pricingPort;

    private final PricingWebMapper pricingWebMapper;

    public PriceQuoteApiServiceAdapter(final PricingPort pricingPort, final PricingWebMapper pricingWebMapper) {
        this.pricingPort = pricingPort;
        this.pricingWebMapper = pricingWebMapper;
    }

    @Override
    public PriceQuoteDto quote(final PriceQuoteRequestDto request) {
        try {
            return this.pricingWebMapper.toDto(this.pricingPort.quote(this.pricingWebMapper.toCommand(request)));
        } catch (final ShipmentNotPriceableException exception) {
            throw new ShipmentNotPriceableApiException(exception.getMessage());
        } catch (final PriceListException exception) {
            if (PriceListException.Reason.NOT_FOUND.equals(exception.reason())) {
                throw new PriceListNotFoundApiException(exception.getMessage());
            }
            throw exception;
        }
    }
}
