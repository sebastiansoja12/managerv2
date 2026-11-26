package com.warehouse.billing.pricing.application.port.primary;

import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.application.port.primary.command.ReplacePriceListCommand;
import com.warehouse.billing.pricing.application.port.secondary.PriceListRepository;
import com.warehouse.billing.pricing.application.port.secondary.PriceQuoteRepository;
import com.warehouse.billing.pricing.domain.enumeration.DeliveryOption;
import com.warehouse.billing.pricing.domain.enumeration.ServiceLevel;
import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.service.PriceCalculator;
import com.warehouse.billing.pricing.domain.service.PriceListFactory;
import com.warehouse.billing.pricing.domain.service.PriceListFixture;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PricingPortImplTest {

    private static final OperatorId OPERATOR_ID = new OperatorId(7L);

    private static final Instant NOW = Instant.parse("2026-11-24T12:00:00Z");

    private PriceListRepository priceListRepository;

    private PriceQuoteRepository priceQuoteRepository;

    private PricingPortImpl pricingPort;

    @BeforeEach
    void setUp() {
        this.priceListRepository = mock(PriceListRepository.class);
        this.priceQuoteRepository = mock(PriceQuoteRepository.class);
        this.pricingPort = new PricingPortImpl(this.priceListRepository, this.priceQuoteRepository,
                new PriceCalculator(), new PriceListFactory(), Clock.fixed(NOW, ZoneOffset.UTC));
    }

    @Test
    void shouldPersistQuoteWhenQuotingForShipment() {
        when(this.priceListRepository.findCurrent()).thenReturn(Optional.of(PriceListFixture.defaultPln(OPERATOR_ID)));

        final PriceQuote quote = this.pricingPort.quote(command());

        assertThat(quote.gross().getAmount()).isEqualByComparingTo("17.22");
        verify(this.priceQuoteRepository).create(quote);
    }

    @Test
    void shouldNotPersistQuoteForPreview() {
        when(this.priceListRepository.findCurrent()).thenReturn(Optional.of(PriceListFixture.defaultPln(OPERATOR_ID)));

        final PriceQuote quote = this.pricingPort.preview(command());

        assertThat(quote.gross().getAmount()).isEqualByComparingTo("17.22");
        verify(this.priceQuoteRepository, never()).create(any());
    }

    @Test
    void shouldReplacePriceList() {
        final PriceList current = PriceListFixture.defaultPln(OPERATOR_ID);
        when(this.priceListRepository.findCurrent()).thenReturn(Optional.of(current));
        final PriceList replacement = PriceListFixture.defaultPln(OPERATOR_ID);

        final PriceList replaced = this.pricingPort.replacePriceList(replaceCommand(replacement,
                new BigDecimal("2.5"), serviceLevels(replacement)));

        assertThat(replaced.insurancePercent()).isEqualByComparingTo("2.5");
        assertThat(replaced.updatedAt()).isEqualTo(NOW);
        verify(this.priceListRepository).update(current);
    }

    @Test
    void shouldRejectReplacementMissingServiceLevel() {
        final PriceList current = PriceListFixture.defaultPln(OPERATOR_ID);
        when(this.priceListRepository.findCurrent()).thenReturn(Optional.of(current));
        final Map<ServiceLevel, BigDecimal> incomplete = serviceLevels(current);
        incomplete.remove(ServiceLevel.EXPRESS);

        assertThatThrownBy(() -> this.pricingPort.replacePriceList(replaceCommand(current, BigDecimal.ONE,
                incomplete))).isInstanceOf(PriceListException.class);
        verify(this.priceListRepository, never()).update(any());
    }

    @Test
    void shouldCopyTemplateForNewOperator() {
        final PriceList template = PriceListFixture.defaultPln(null);
        when(this.priceListRepository.findByOperator(OPERATOR_ID)).thenReturn(Optional.empty());
        when(this.priceListRepository.findTemplate()).thenReturn(Optional.of(template));

        this.pricingPort.createDefaultPriceList(OPERATOR_ID);

        verify(this.priceListRepository).create(org.mockito.ArgumentMatchers.argThat(created ->
                OPERATOR_ID.equals(created.operatorId()) && created.sizeClasses().equals(template.sizeClasses())));
    }

    @Test
    void shouldCreateDefaultPriceListOnlyOnce() {
        when(this.priceListRepository.findByOperator(OPERATOR_ID))
                .thenReturn(Optional.of(PriceListFixture.defaultPln(OPERATOR_ID)));

        this.pricingPort.createDefaultPriceList(OPERATOR_ID);

        verify(this.priceListRepository, never()).create(any());
    }

    private QuoteShipmentCommand command() {
        return new QuoteShipmentCommand(PriceListFixture.input(30, 20, 10, 2, ServiceLevel.STANDARD,
                DeliveryOption.PICKUP_POINT, (Money) null));
    }

    private ReplacePriceListCommand replaceCommand(final PriceList source, final BigDecimal insurancePercent,
                                                   final Map<ServiceLevel, BigDecimal> serviceLevels) {
        return new ReplacePriceListCommand(source.currency(), source.sizeClasses(), source.weightBands(),
                serviceLevels, source.deliveryOptionSurcharges(), insurancePercent, source.vatPercent());
    }

    private Map<ServiceLevel, BigDecimal> serviceLevels(final PriceList source) {
        return new EnumMap<>(source.serviceLevelMultipliers());
    }
}
