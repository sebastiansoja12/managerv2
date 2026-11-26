package com.warehouse.billing.pricing.infrastructure.adapter.secondary.mapper;

import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.model.Money;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceListEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceQuoteEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceQuoteLineEmbeddable;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.SizeClassEmbeddable;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.WeightBandEmbeddable;

public class PricingPersistenceMapper {

    public PriceListEntity toEntity(final PriceList priceList) {
        final PriceListEntity entity = new PriceListEntity(priceList.id(), priceList.operatorId() == null,
                priceList.currency(), priceList.insurancePercent(), priceList.vatPercent(), priceList.updatedAt(),
                priceList.sizeClasses().stream()
                        .map(sizeClass -> new SizeClassEmbeddable(sizeClass.code(), sizeClass.maxLengthCm(),
                                sizeClass.maxWidthCm(), sizeClass.maxHeightCm(), sizeClass.netPrice()))
                        .toList(),
                priceList.weightBands().stream()
                        .map(weightBand -> new WeightBandEmbeddable(weightBand.fromKgInclusive(),
                                weightBand.toKgExclusive(), weightBand.netSurcharge()))
                        .toList(),
                priceList.serviceLevelMultipliers(), priceList.deliveryOptionSurcharges());
        entity.assignOperator(priceList.operatorId());
        return entity;
    }

    public PriceList toDomain(final PriceListEntity entity) {
        return new PriceList(entity.getPriceListId(), entity.operatorId(), entity.getCurrency(),
                entity.getSizeClasses().stream()
                        .map(sizeClass -> new SizeClass(sizeClass.getCode(), sizeClass.getMaxLengthCm(),
                                sizeClass.getMaxWidthCm(), sizeClass.getMaxHeightCm(), sizeClass.getNetPrice()))
                        .toList(),
                entity.getWeightBands().stream()
                        .map(weightBand -> new WeightBand(weightBand.getFromKgInclusive(),
                                weightBand.getToKgExclusive(), weightBand.getNetSurcharge()))
                        .toList(),
                entity.getServiceLevelMultipliers(), entity.getDeliveryOptionSurcharges(),
                entity.getInsurancePercent(), entity.getVatPercent(), entity.getUpdatedAt());
    }

    public PriceQuoteEntity toEntity(final PriceQuote quote) {
        return new PriceQuoteEntity(quote.id(), quote.priceListId(), quote.currency(), quote.vatPercent(),
                quote.quotedAt(),
                quote.lines().stream()
                        .map(line -> new PriceQuoteLineEmbeddable(line.type(), line.description(),
                                line.net().getAmount(), line.vat().getAmount(), line.gross().getAmount()))
                        .toList());
    }

    public PriceQuote toDomain(final PriceQuoteEntity entity) {
        return new PriceQuote(entity.getPriceQuoteId(), entity.getPriceListId(), entity.getCurrency(),
                entity.getLines().stream()
                        .map(line -> new PriceQuoteLine(line.getType(), line.getDescription(),
                                Money.from(line.getNet(), entity.getCurrency()),
                                Money.from(line.getVat(), entity.getCurrency()),
                                Money.from(line.getGross(), entity.getCurrency())))
                        .toList(),
                entity.getVatPercent(), entity.getQuotedAt());
    }
}
