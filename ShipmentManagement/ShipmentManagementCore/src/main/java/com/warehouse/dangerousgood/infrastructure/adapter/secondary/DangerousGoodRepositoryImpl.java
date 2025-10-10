package com.warehouse.dangerousgood.infrastructure.adapter.secondary;

import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.port.secondary.DangerousGoodRepository;
import com.warehouse.dangerousgood.domain.vo.DangerousGoodId;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity.DangerousGoodEntity;

public class DangerousGoodRepositoryImpl implements DangerousGoodRepository {

    private final DangerousGoodReadRepository repository;

    public DangerousGoodRepositoryImpl(final DangerousGoodReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createOrUpdate(final DangerousGood dangerousGood) {
        final DangerousGoodEntity entity = DangerousGoodEntity.from(dangerousGood);
        this.repository.save(entity);
    }

    @Override
    public DangerousGood findById(final DangerousGoodId dangerousGoodId) {
        return this.repository.findById(dangerousGoodId).map(DangerousGood::from).orElse(null);
    }
}
