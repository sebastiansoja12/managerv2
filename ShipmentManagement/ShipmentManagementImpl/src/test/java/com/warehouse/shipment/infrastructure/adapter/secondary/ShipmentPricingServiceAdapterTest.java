package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.billing.api.PriceQuoteApiService;
import com.warehouse.billing.api.dto.DeliveryOptionDto;
import com.warehouse.billing.api.dto.PriceQuoteDto;
import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;
import com.warehouse.billing.api.dto.ServiceLevelDto;
import com.warehouse.billing.api.exception.PriceListNotFoundApiException;
import com.warehouse.billing.api.exception.ShipmentNotPriceableApiException;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.application.port.primary.command.ShipmentCreateCommand;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;
import com.warehouse.shipment.domain.vo.Dimensions;
import com.warehouse.shipment.domain.vo.LengthUnit;
import com.warehouse.shipment.domain.vo.PricedShipment;
import com.warehouse.shipment.domain.vo.Weight;
import com.warehouse.shipment.domain.vo.WeightUnit;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import com.warehouse.shipment.infrastructure.adapter.secondary.mapper.ShipmentPricingRequestMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ShipmentPricingServiceAdapterTest {

    private final PriceQuoteApiService priceQuoteApiService = mock(PriceQuoteApiService.class);

    private final ShipmentPricingServiceAdapter adapter = new ShipmentPricingServiceAdapter(this.priceQuoteApiService,
            new ShipmentPricingRequestMapper());

    @Test
    void shouldQuoteShipmentInCentimetersAndKilograms() {
        final ShipmentCreateCommand command = command(new Dimensions(new BigDecimal("400"), new BigDecimal("300"),
                new BigDecimal("200"), LengthUnit.MM), new Weight(new BigDecimal("2500"), WeightUnit.G));
        command.setDeliveryMethod(DeliveryMethod.LOCKER);
        command.setDeclaredValue(new Money(new BigDecimal("1000.00"), Currency.PLN));
        when(this.priceQuoteApiService.quote(any(PriceQuoteRequestDto.class))).thenReturn(new PriceQuoteDto(
                new PriceQuoteIdDto(5L), new BigDecimal("20.00"), new BigDecimal("4.60"), new BigDecimal("24.60"),
                Currency.PLN));
        final ArgumentCaptor<PriceQuoteRequestDto> requestCaptor = ArgumentCaptor.forClass(PriceQuoteRequestDto.class);

        final Result<PricedShipment, ErrorCode> result = this.adapter.quote(command, ShipmentServiceLevel.EXPRESS);

        verify(this.priceQuoteApiService).quote(requestCaptor.capture());
        final PriceQuoteRequestDto request = requestCaptor.getValue();
        assertThat(request.lengthCm()).isEqualByComparingTo("40");
        assertThat(request.widthCm()).isEqualByComparingTo("30");
        assertThat(request.heightCm()).isEqualByComparingTo("20");
        assertThat(request.weightKg()).isEqualByComparingTo("2.5");
        assertThat(request.serviceLevel()).isEqualTo(ServiceLevelDto.EXPRESS);
        assertThat(request.deliveryOption()).isEqualTo(DeliveryOptionDto.LOCKER);
        assertThat(request.declaredValueAmount()).isEqualByComparingTo("1000.00");
        assertThat(request.declaredValueCurrency()).isEqualTo(Currency.PLN);
        assertThat(result.getSuccess().quoteId()).isEqualTo(new PriceQuoteId(5L));
        assertThat(result.getSuccess().gross().getAmount()).isEqualByComparingTo("24.60");
        assertThat(result.getSuccess().gross().getCurrency()).isEqualTo(Currency.PLN);
    }

    @Test
    void shouldConvertMetersAndKeepKilograms() {
        final ShipmentCreateCommand command = command(new Dimensions(new BigDecimal("1.2"), new BigDecimal("0.5"),
                new BigDecimal("0.25"), LengthUnit.M), new Weight(new BigDecimal("3"), WeightUnit.KG));
        when(this.priceQuoteApiService.quote(any(PriceQuoteRequestDto.class))).thenReturn(new PriceQuoteDto(
                new PriceQuoteIdDto(6L), BigDecimal.TEN, BigDecimal.ONE, new BigDecimal("11"), Currency.PLN));
        final ArgumentCaptor<PriceQuoteRequestDto> requestCaptor = ArgumentCaptor.forClass(PriceQuoteRequestDto.class);

        this.adapter.quote(command, ShipmentServiceLevel.STANDARD);

        verify(this.priceQuoteApiService).quote(requestCaptor.capture());
        assertThat(requestCaptor.getValue().lengthCm()).isEqualByComparingTo("120");
        assertThat(requestCaptor.getValue().widthCm()).isEqualByComparingTo("50");
        assertThat(requestCaptor.getValue().heightCm()).isEqualByComparingTo("25");
        assertThat(requestCaptor.getValue().weightKg()).isEqualByComparingTo("3");
        assertThat(requestCaptor.getValue().deliveryOption()).isEqualTo(DeliveryOptionDto.COURIER);
        assertThat(requestCaptor.getValue().declaredValueAmount()).isNull();
    }

    @Test
    void shouldReturnNotPriceableWhenShipmentExceedsPriceList() {
        when(this.priceQuoteApiService.quote(any(PriceQuoteRequestDto.class)))
                .thenThrow(new ShipmentNotPriceableApiException("Shipment exceeds every size class"));

        final Result<PricedShipment, ErrorCode> result = this.adapter.quote(defaultCommand(),
                ShipmentServiceLevel.STANDARD);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).isEqualTo(ErrorCode.SHIPMENT_NOT_PRICEABLE);
    }

    @Test
    void shouldReturnNotPriceableWhenPriceListIsMissing() {
        when(this.priceQuoteApiService.quote(any(PriceQuoteRequestDto.class)))
                .thenThrow(new PriceListNotFoundApiException("Price list not found for the current operator"));

        final Result<PricedShipment, ErrorCode> result = this.adapter.quote(defaultCommand(),
                ShipmentServiceLevel.STANDARD);

        assertThat(result.isFailure()).isTrue();
        assertThat(result.getFailure()).isEqualTo(ErrorCode.SHIPMENT_NOT_PRICEABLE);
    }

    private ShipmentCreateCommand defaultCommand() {
        return command(new Dimensions(BigDecimal.TEN, BigDecimal.TEN, BigDecimal.TEN, LengthUnit.CM),
                new Weight(BigDecimal.ONE, WeightUnit.KG));
    }

    private ShipmentCreateCommand command(final Dimensions dimensions, final Weight weight) {
        final ShipmentCreateCommand command = new ShipmentCreateCommand();
        command.setDimensions(dimensions);
        command.setWeight(weight);
        return command;
    }
}
