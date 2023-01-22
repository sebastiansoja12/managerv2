package com.warehouse.depot.infrastructure.secondary;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.infrastructure.secondary.entity.DepotEntity;
import com.warehouse.depot.infrastructure.secondary.exception.DepotNotFoundException;
import com.warehouse.depot.infrastructure.secondary.mapper.DepotMapper;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DepotRepositoryImpl implements DepotRepository {

    private final DepotReadRepository repository;

    private final DepotMapper depotMapper;

    @Override
    public void save(Depot depot) {
        final DepotEntity depotEntity = depotMapper.map(depot);
        repository.save(depotEntity);
    }

    @Override
    public Depot viewByCode(DepotCode depotCode) {
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode.getValue());
        return depot.map(depotMapper::map).orElseThrow(
                () -> new DepotNotFoundException("Depot was not found")
        );
    }

    @Override
    public Depot viewById(DepotId depotId) {
        final Optional<DepotEntity> depot = repository.findById(depotId.getValue());
        return depot.map(depotMapper::map).orElseThrow(
                () -> new DepotNotFoundException("Depot was not found")
        );
    }

    @Override
    public List<Depot> findAll() {
        final List<DepotEntity> depots = repository.findAll();
        return depotMapper.map(depots);
    }

    @Override
    public void saveAll(List<Depot> depots) {
        final List<DepotEntity> depotEntities = depotMapper.mapToDepotEntityList(depots);
        repository.saveAll(depotEntities);
    }
}
