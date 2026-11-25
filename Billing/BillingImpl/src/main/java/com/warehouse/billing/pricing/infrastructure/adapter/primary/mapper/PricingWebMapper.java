package com.warehouse.billing.pricing.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.dto.PriceQuoteDto;
import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.api.dto.PriceQuoteRequestDto;
import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.application.port.primary.command.ReplacePriceListCommand;
import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PricingInput;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceListApiResponse;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceListIdDto;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceQuoteApiRequest;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceQuoteApiResponse;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.PriceQuoteLineApi;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.ReplacePriceListApiRequest;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.SizeClassApi;
import com.warehouse.billing.pricing.infrastructure.adapter.primary.api.WeightBandApi;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;

public class PricingWebMapper {

    public QuoteShipmentCommand toCommand(final PriceQuoteApiRequest request) {
        return toCommand(request.lengthCm(), request.widthCm(), request.heightCm(), request.weightKg(),
                request.serviceLevel(), request.deliveryOption(), request.declaredValueAmount(),
                request.declaredValueCurrency());
    }

    public QuoteShipmentCommand toCommand(final PriceQuoteRequestDto request) {
        return toCommand(request.lengthCm(), request.widthCm(), request.heightCm(), request.weightKg(),
                ServiceLevel.valueOf(request.serviceLevel().name()),
                DeliveryOption.valueOf(request.deliveryOption().name()), request.declaredValueAmount(),
                request.declaredValueCurrency());
    }

    public ReplacePriceListCommand toCommand(final ReplacePriceListApiRequest request) {
        return new ReplacePriceListCommand(request.currency(),
                request.sizeClasses().stream()
                        .map(sizeClass -> new SizeClass(sizeClass.code(), sizeClass.maxLengthCm(),
                                sizeClass.maxWidthCm(), sizeClass.maxHeightCm(), sizeClass.netPrice()))
                        .toList(),
                request.weightBands().stream()
                        .map(weightBand -> new WeightBand(weightBand.fromKgInclusive(), weightBand.toKgExclusive(),
                                weightBand.netSurcharge()))
                        .toList(),
                request.serviceLevelMultipliers(), request.deliveryOptionSurcharges(), request.insurancePercent(),
                request.vatPercent());
    }

    public PriceListApiResponse toResponse(final PriceList priceList) {
        return new PriceListApiResponse(new PriceListIdDto(priceList.id().value()), priceList.currency(),
                priceList.sizeClasses().stream()
                        .map(sizeClass -> new SizeClassApi(sizeClass.code(), sizeClass.maxLengthCm(),
                                sizeClass.maxWidthCm(), sizeClass.maxHeightCm(), sizeClass.netPrice()))
                        .toList(),
                priceList.weightBands().stream()
                        .map(weightBand -> new WeightBandApi(weightBand.fromKgInclusive(),
                                weightBand.toKgExclusive(), weightBand.netSurcharge()))
                        .toList(),
                priceList.serviceLevelMultipliers(), priceList.deliveryOptionSurcharges(),
                priceList.insurancePercent(), priceList.vatPercent(), priceList.updatedAt());
    }

    public PriceQuoteApiResponse toResponse(final PriceQuote quote) {
        return new PriceQuoteApiResponse(
                quote.lines().stream()
                        .map(line -> new PriceQuoteLineApi(line.type(), line.description(),
                                line.net().getAmount(), line.vat().getAmount(), line.gross().getAmount()))
                        .toList(),
                quote.net().getAmount(), quote.vat().getAmount(), quote.gross().getAmount(), quote.vatPercent(),
                quote.currency());
    }

    public PriceQuoteDto toDto(final PriceQuote quote) {
        return new PriceQuoteDto(new PriceQuoteIdDto(quote.id().value()), quote.net().getAmount(),
                quote.vat().getAmount(), quote.gross().getAmount(), quote.currency());
    }

    private QuoteShipmentCommand toCommand(final BigDecimal lengthCm, final BigDecimal widthCm,
                                           final BigDecimal heightCm, final BigDecimal weightKg,
                                           final ServiceLevel serviceLevel, final DeliveryOption deliveryOption,
                                           final BigDecimal declaredValueAmount, final Currency declaredValueCurrency) {
        final Money declaredValue = declaredValueAmount == null
                ? null : Money.from(declaredValueAmount, declaredValueCurrency);
        return new QuoteShipmentCommand(new PricingInput(lengthCm, widthCm, heightCm, weightKg, serviceLevel,
                deliveryOption, declaredValue));
    }
}
