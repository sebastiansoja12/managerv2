package com.warehouse.billing.pricing.infrastructure.adapter.secondary;

import com.warehouse.billing.pricing.application.port.secondary.PriceQuoteRepository;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceQuoteEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.mapper.PricingPersistenceMapper;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.Optional;

public class PriceQuoteRepositoryImpl implements PriceQuoteRepository {

    private final OperatorFilteredRepository<PriceQuoteEntity> repository;

    private final PricingPersistenceMapper persistenceMapper;

    public PriceQuoteRepositoryImpl(final OperatorFilteredRepository<PriceQuoteEntity> repository,
                                    final PricingPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final PriceQuote quote) {
        this.repository.create(this.persistenceMapper.toEntity(quote));
    }

    @Override
    public Optional<PriceQuote> findById(final PriceQuoteId quoteId) {
        return this.repository.createCriteria(PriceQuoteEntity.class)
                .eq("priceQuoteId.value", quoteId.value())
                .one()
                .map(this.persistenceMapper::toDomain);
    }
}
