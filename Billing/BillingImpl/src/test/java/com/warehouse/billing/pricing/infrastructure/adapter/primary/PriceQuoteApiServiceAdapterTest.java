package com.warehouse.billing.pricing.infrastructure.adapter.primary;

import com.warehouse.billing.api.dto.DeliveryOptionDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;
import com.warehouse.billing.api.dto.ServiceLevelDto;
import com.warehouse.billing.api.exception.PriceListNotFoundApiException;
import com.warehouse.billing.api.exception.ShipmentNotPriceableApiException;
import com.warehouse.billing.pricing.application.port.primary.PricingPort;
import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper.PricingWebMapper;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PriceQuoteApiServiceAdapterTest {

    @Test
    void shouldTranslateNotPriceableToApiException() {
        final PricingPort pricingPort = mock(PricingPort.class);
        when(pricingPort.quote(any(QuoteShipmentCommand.class)))
                .thenThrow(new ShipmentNotPriceableException("Shipment exceeds every size class"));
        final PriceQuoteApiServiceAdapter adapter = new PriceQuoteApiServiceAdapter(pricingPort,
                new PricingWebMapper());

        assertThatThrownBy(() -> adapter.quote(new PriceQuoteRequestDto(new BigDecimal("500"),
                new BigDecimal("500"), new BigDecimal("500"), BigDecimal.ONE, ServiceLevelDto.STANDARD,
                DeliveryOptionDto.COURIER, null, null)))
                .isInstanceOf(ShipmentNotPriceableApiException.class)
                .hasMessage("Shipment exceeds every size class");
    }

    @Test
    void shouldTranslateMissingPriceListToApiException() {
        final PricingPort pricingPort = mock(PricingPort.class);
        when(pricingPort.quote(any(QuoteShipmentCommand.class)))
                .thenThrow(new PriceListException(PriceListException.Reason.NOT_FOUND,
                        "Price list not found for the current operator"));
        final PriceQuoteApiServiceAdapter adapter = new PriceQuoteApiServiceAdapter(pricingPort,
                new PricingWebMapper());

        assertThatThrownBy(() -> adapter.quote(new PriceQuoteRequestDto(BigDecimal.TEN, BigDecimal.TEN,
                BigDecimal.TEN, BigDecimal.ONE, ServiceLevelDto.STANDARD, DeliveryOptionDto.COURIER, null, null)))
                .isInstanceOf(PriceListNotFoundApiException.class)
                .hasMessage("Price list not found for the current operator");
    }
}
