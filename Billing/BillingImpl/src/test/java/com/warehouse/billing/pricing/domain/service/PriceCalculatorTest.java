package com.warehouse.billing.pricing.domain.service;

import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PriceCalculatorTest {

    private final PriceCalculator calculator = new PriceCalculator();

    @Test
    void shouldQuoteStandardCourierShipmentWithoutInsurance() {
        final PriceQuote quote = calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(10, 30, 50, 4.0, ServiceLevel.STANDARD, DeliveryOption.COURIER, null));

        assertThat(quote.lines()).extracting(PriceQuoteLine::type)
                .containsExactly(PriceQuoteLineType.BASE, PriceQuoteLineType.DELIVERY_METHOD);
        assertThat(quote.net().getAmount()).isEqualByComparingTo("18.00");
        assertThat(quote.vat().getAmount()).isEqualByComparingTo("4.14");
        assertThat(quote.gross().getAmount()).isEqualByComparingTo("22.14");
    }

    @Test
    void shouldApplyExpressMultiplierAndInsurance() {
        final PriceQuote quote = calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(8, 38, 64, 7.0, ServiceLevel.EXPRESS, DeliveryOption.LOCKER,
                        Money.from(new BigDecimal("500.00"), Currency.PLN)));

        assertThat(quote.lines()).extracting(PriceQuoteLine::type).containsExactly(PriceQuoteLineType.BASE,
                PriceQuoteLineType.WEIGHT_SURCHARGE, PriceQuoteLineType.SERVICE_LEVEL,
                PriceQuoteLineType.INSURANCE);
        assertThat(quote.net().getAmount()).isEqualByComparingTo("27.50");
    }

    @Test
    void shouldRejectShipmentLargerThanEverySizeClass() {
        assertThatThrownBy(() -> calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(100, 100, 130, 1.0, ServiceLevel.STANDARD, DeliveryOption.COURIER, null)))
                .isInstanceOf(ShipmentNotPriceableException.class);
    }

    @Test
    void shouldRejectShipmentHeavierThanEveryWeightBand() {
        assertThatThrownBy(() -> calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(10, 30, 50, 50.0, ServiceLevel.STANDARD, DeliveryOption.COURIER, null)))
                .isInstanceOf(ShipmentNotPriceableException.class);
    }

    @Test
    void shouldFitRotatedShipmentIntoSizeClass() {
        final PriceQuote quote = calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(64, 8, 38, 1.0, ServiceLevel.STANDARD, DeliveryOption.PICKUP_POINT, null));

        assertThat(quote.lines()).extracting(PriceQuoteLine::type).containsExactly(PriceQuoteLineType.BASE);
        assertThat(quote.lines().get(0).net().getAmount()).isEqualByComparingTo("12.00");
    }

    @Test
    void shouldCopyPriceListForOperatorWithNewId() {
        final PriceList template = PriceListFixture.defaultPln();

        final PriceList copy = new PriceListFactory().copyForOperator(template, new OperatorId(7L));

        assertThat(copy.id()).isNotEqualTo(template.id());
        assertThat(copy.operatorId()).isEqualTo(new OperatorId(7L));
        assertThat(copy.sizeClasses()).isEqualTo(template.sizeClasses());
        assertThat(copy.vatPercent()).isEqualByComparingTo("23");
    }

    @Test
    void shouldRejectDeclaredValueInDifferentCurrency() {
        assertThatThrownBy(() -> calculator.quote(PriceListFixture.defaultPln(),
                PriceListFixture.input(8, 38, 64, 1.0, ServiceLevel.STANDARD, DeliveryOption.COURIER,
                        Money.from(new BigDecimal("500.00"), Currency.EUR))))
                .isInstanceOf(ShipmentNotPriceableException.class);
    }
}
