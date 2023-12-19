package com.warehouse.depot.infrastructure.adapter.secondary;

import com.warehouse.depot.domain.vo.Depot;
import com.warehouse.depot.domain.vo.DepotCode;
import com.warehouse.depot.domain.port.secondary.DepotRepository;
import com.warehouse.depot.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.depot.infrastructure.adapter.secondary.exception.DepotNotFoundException;
import com.warehouse.depot.infrastructure.adapter.secondary.mapper.DepotMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DepotRepositoryImpl implements DepotRepository {

    private final DepotReadRepository repository;

    private final DepotMapper depotMapper;

    @Override
    @Cacheable("depotCodeCache")
    public Depot viewByCode(DepotCode depotCode) {
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode.getValue());
        return depot.map(depotMapper::map).orElseThrow(() -> new DepotNotFoundException("Depot was not found"));
    }

    @Override
    @Cacheable("depotsCache")
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
