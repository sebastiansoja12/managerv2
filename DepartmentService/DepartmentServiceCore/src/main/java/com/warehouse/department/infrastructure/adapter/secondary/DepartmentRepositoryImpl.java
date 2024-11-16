package com.warehouse.department.infrastructure.adapter.secondary;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.domain.vo.DepartmentCode;
import com.warehouse.department.domain.port.secondary.DepartmentRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.department.infrastructure.adapter.secondary.exception.DepotNotFoundException;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    private final DepotMapper depotMapper;

    @Override
    @Cacheable("depotCodeCache")
    public Department findByCode(DepartmentCode departmentCode) {
        final Optional<DepartmentEntity> depot = repository.findByDepotCode(departmentCode.getValue());
        return depot.map(depotMapper::map).orElseThrow(() -> new DepotNotFoundException("Department was not found"));
    }

    @Override
    @Cacheable("depotsCache")
    public List<Department> findAll() {
        final List<DepartmentEntity> depots = repository.findAll();
        return depotMapper.map(depots);
    }

    @Override
    public void save(final Department department) {
        final DepartmentEntity departmentEntity = depotMapper.map(department);
        repository.save(departmentEntity);
    }

    @Override
    public void saveAll(final List<Department> departments) {
        final List<DepartmentEntity> depotEntities = depotMapper.mapToDepotEntityList(departments);
        repository.saveAll(depotEntities);
    }
}
