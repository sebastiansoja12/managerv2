package com.warehouse.billing.pricing.infrastructure.adapter.secondary;

import com.warehouse.billing.pricing.application.port.secondary.PriceListRepository;
import com.warehouse.billing.pricing.domain.model.PriceList;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.entity.PriceListEntity;
import com.warehouse.billing.pricing.infrastructure.adapter.secondary.mapper.PricingPersistenceMapper;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.NoOperatorContextRepository;
import com.warehouse.commonassets.repository.OperatorFilteredRepository;

import java.util.Optional;

public class PriceListRepositoryImpl implements PriceListRepository {

    private final OperatorFilteredRepository<PriceListEntity> repository;

    private final NoOperatorContextRepository<PriceListEntity> platformRepository;

    private final PricingPersistenceMapper persistenceMapper;

    public PriceListRepositoryImpl(final OperatorFilteredRepository<PriceListEntity> repository,
                                   final NoOperatorContextRepository<PriceListEntity> platformRepository,
                                   final PricingPersistenceMapper persistenceMapper) {
        this.repository = repository;
        this.platformRepository = platformRepository;
        this.persistenceMapper = persistenceMapper;
    }

    @Override
    public void create(final PriceList priceList) {
        this.repository.create(this.persistenceMapper.toEntity(priceList));
    }

    @Override
    public void update(final PriceList priceList) {
        this.repository.update(this.persistenceMapper.toEntity(priceList));
    }

    @Override
    public Optional<PriceList> findCurrent() {
        return this.repository.createCriteria(PriceListEntity.class)
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<PriceList> findByOperator(final OperatorId operatorId) {
        return this.platformRepository.createCriteria(PriceListEntity.class)
                .eq("operatorId.value", operatorId.getValue())
                .one()
                .map(this.persistenceMapper::toDomain);
    }

    @Override
    public Optional<PriceList> findTemplate() {
        return this.platformRepository.createCriteria(PriceListEntity.class)
                .isTrue("template")
                .one()
                .map(this.persistenceMapper::toDomain);
    }
}
