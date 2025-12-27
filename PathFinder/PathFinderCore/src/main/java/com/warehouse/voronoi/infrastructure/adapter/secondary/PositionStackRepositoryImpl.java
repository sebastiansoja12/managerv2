package com.warehouse.voronoi.infrastructure.adapter.secondary;

import com.warehouse.voronoi.domain.model.PositionStack;
import com.warehouse.voronoi.domain.port.secondary.PositionStackRepository;
import com.warehouse.voronoi.infrastructure.adapter.secondary.entity.PositionStackEntity;


public class PositionStackRepositoryImpl implements PositionStackRepository {

    private final PositionStackReadRepository repository;

    public PositionStackRepositoryImpl(final PositionStackReadRepository repository) {
        this.repository = repository;
    }
    @Override
    public PositionStack findPositionStackConfiguration() {
        return repository.findAll().stream().filter(PositionStackEntity::isValid)
                .findAny()
                .map(pos -> new PositionStack(pos.getId(), pos.getToken(), pos.isValid()))
                .orElse(null);
    }
}
