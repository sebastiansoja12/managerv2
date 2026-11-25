package com.warehouse.billing.pricing.domain.service;

import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.billing.pricing.domain.vo.PricingInput;
import com.warehouse.billing.pricing.domain.vo.SizeClass;
import com.warehouse.billing.pricing.domain.vo.WeightBand;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceCalculator {

    public PriceQuote quote(final PriceList priceList, final PricingInput input) {
        if (input.declaredValue() != null && input.declaredValue().getCurrency() != priceList.currency()) {
            throw new ShipmentNotPriceableException("Declared value currency differs from the price list currency");
        }
        final SizeClass sizeClass = priceList.sizeClasses().stream()
                .sorted(Comparator.comparing(SizeClass::netPrice))
                .filter(candidate -> candidate.fits(input.lengthCm(), input.widthCm(), input.heightCm()))
                .findFirst()
                .orElseThrow(() -> new ShipmentNotPriceableException("Shipment exceeds every size class"));
        final WeightBand weightBand = priceList.weightBands().stream()
                .filter(candidate -> candidate.contains(input.weightKg()))
                .findFirst()
                .orElseThrow(() -> new ShipmentNotPriceableException("Shipment exceeds every weight band"));
        final BigDecimal vatPercent = priceList.vatPercent();
        final Currency currency = priceList.currency();
        final Money base = amount(sizeClass.netPrice(), currency);
        final Money weightSurcharge = amount(weightBand.netSurcharge(), currency);
        final List<PriceQuoteLine> lines = new ArrayList<>();
        lines.add(line(PriceQuoteLineType.BASE, "Size class " + sizeClass.code(), base, vatPercent));
        if (!weightSurcharge.isZero()) {
            lines.add(line(PriceQuoteLineType.WEIGHT_SURCHARGE, "Weight surcharge", weightSurcharge, vatPercent));
        }
        final BigDecimal multiplier = priceList.serviceLevelMultipliers().get(input.serviceLevel());
        if (multiplier.compareTo(BigDecimal.ONE) != 0) {
            lines.add(line(PriceQuoteLineType.SERVICE_LEVEL, "Service level " + input.serviceLevel(),
                    base.add(weightSurcharge).multiply(multiplier.subtract(BigDecimal.ONE)), vatPercent));
        }
        final Money deliverySurcharge = amount(priceList.deliveryOptionSurcharges().get(input.deliveryOption()),
                currency);
        if (!deliverySurcharge.isZero()) {
            lines.add(line(PriceQuoteLineType.DELIVERY_METHOD, "Delivery " + input.deliveryOption(),
                    deliverySurcharge, vatPercent));
        }
        if (input.declaredValue() != null && input.declaredValue().isPositive()) {
            lines.add(line(PriceQuoteLineType.INSURANCE, "Insurance",
                    input.declaredValue().percentage(priceList.insurancePercent()), vatPercent));
        }
        return new PriceQuote(PriceQuoteId.nextId(), priceList.id(), priceList.currency(), lines, vatPercent,
                Instant.now());
    }

    private Money amount(final BigDecimal value, final Currency currency) {
        return Money.from(value.setScale(2, RoundingMode.HALF_UP), currency);
    }

    private PriceQuoteLine line(final PriceQuoteLineType type, final String description, final Money net,
                                final BigDecimal vatPercent) {
        final Money vat = net.percentage(vatPercent);
        return new PriceQuoteLine(type, description, net, vat, net.add(vat));
    }
}
