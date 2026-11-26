package com.warehouse.billing.pricing.application.port.primary;

import com.warehouse.billing.pricing.application.port.primary.command.QuoteShipmentCommand;
import com.warehouse.billing.pricing.application.port.primary.command.ReplacePriceListCommand;
import com.warehouse.billing.pricing.application.port.secondary.PriceListRepository;
import com.warehouse.billing.pricing.application.port.secondary.PriceQuoteRepository;
import com.warehouse.billing.pricing.domain.exception.PriceListException;
import com.warehouse.billing.pricing.domain.exception.ShipmentNotPriceableException;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.service.PriceCalculator;
import com.warehouse.billing.pricing.domain.service.PriceListFactory;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.util.Optional;

public class PricingPortImpl implements PricingPort {

    private final PriceListRepository priceListRepository;

    private final PriceQuoteRepository priceQuoteRepository;

    private final PriceCalculator priceCalculator;

    private final PriceListFactory priceListFactory;

    private final Clock clock;

    public PricingPortImpl(final PriceListRepository priceListRepository,
                           final PriceQuoteRepository priceQuoteRepository,
                           final PriceCalculator priceCalculator,
                           final PriceListFactory priceListFactory,
                           final Clock clock) {
        this.priceListRepository = priceListRepository;
        this.priceQuoteRepository = priceQuoteRepository;
        this.priceCalculator = priceCalculator;
        this.priceListFactory = priceListFactory;
        this.clock = clock;
    }

    @Override
    @Transactional(noRollbackFor = {ShipmentNotPriceableException.class, PriceListException.class})
    public PriceQuote quote(final QuoteShipmentCommand command) {
        final PriceQuote quote = preview(command);
        this.priceQuoteRepository.create(quote);
        return quote;
    }

    @Override
    @Transactional(readOnly = true)
    public PriceQuote preview(final QuoteShipmentCommand command) {
        return this.priceCalculator.quote(getPriceList(), command.input());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PriceQuote> findQuote(final PriceQuoteId quoteId) {
        return this.priceQuoteRepository.findById(quoteId);
    }

    @Override
    @Transactional(readOnly = true)
    public PriceList getPriceList() {
        return this.priceListRepository.findCurrent()
                .orElseThrow(() -> new PriceListException(PriceListException.Reason.NOT_FOUND,
                        "Price list not found for the current operator"));
    }

    @Override
    @Transactional
    public PriceList replacePriceList(final ReplacePriceListCommand command) {
        final PriceList priceList = getPriceList();
        priceList.replace(new PriceList(priceList.id(), priceList.operatorId(), command.currency(),
                command.sizeClasses(), command.weightBands(), command.serviceLevelMultipliers(),
                command.deliveryOptionSurcharges(), command.insurancePercent(), command.vatPercent(),
                this.clock.instant()));
        this.priceListRepository.update(priceList);
        return priceList;
    }

    @Override
    @Transactional
    public void createDefaultPriceList(final OperatorId operatorId) {
        if (this.priceListRepository.findByOperator(operatorId).isPresent()) {
            return;
        }
        this.priceListRepository.create(this.priceListFactory.copyForOperator(findTemplate(), operatorId));
    }

    @Override
    @Transactional(readOnly = true)
    public PriceList findTemplate() {
        return this.priceListRepository.findTemplate()
                .orElseThrow(() -> new PriceListException(PriceListException.Reason.NOT_FOUND,
                        "Template price list not found"));
    }
}
