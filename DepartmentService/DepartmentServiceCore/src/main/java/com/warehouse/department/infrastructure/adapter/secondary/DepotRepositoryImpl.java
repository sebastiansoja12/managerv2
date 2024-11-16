package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepotCode;
import com.warehouse.department.domain.port.secondary.DepotRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.department.infrastructure.adapter.secondary.exception.DepotNotFoundException;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;
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
    public Department findByCode(DepotCode depotCode) {
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode.getValue());
        return depot.map(depotMapper::map).orElseThrow(() -> new DepotNotFoundException("Depot was not found"));
    }

    @Override
    @Cacheable("depotsCache")
    public List<Department> findAll() {
        final List<DepotEntity> depots = repository.findAll();
        return depotMapper.map(depots);
    }

    @Override
    public void save(final Department department) {
        final DepotEntity depotEntity = depotMapper.map(department);
        repository.save(depotEntity);
    }

    @Override
    public void saveAll(final List<Department> departments) {
        final List<DepotEntity> depotEntities = depotMapper.mapToDepotEntityList(departments);
        repository.saveAll(depotEntities);
    }
}
