package com.warehouse.dangerousgood.infrastructure.adapter.secondary;

import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.port.secondary.DangerousGoodRepository;
import com.warehouse.dangerousgood.infrastructure.adapter.secondary.entity.DangerousGoodEntity;

public class DangerousGoodRepositoryImpl implements DangerousGoodRepository {

    private final DangerousGoodReadRepository repository;

    public DangerousGoodRepositoryImpl(final DangerousGoodReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public void create(final DangerousGood dangerousGood) {
        final DangerousGoodEntity entity = DangerousGoodEntity.from(dangerousGood);
        this.repository.save(entity);
    }
}
