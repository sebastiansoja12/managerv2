package com.warehouse.terminal.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import java.util.Optional;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.terminal.domain.model.Department;
import com.warehouse.terminal.domain.port.secondary.DepartmentRepository;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.terminal.infrastructure.adapter.secondary.mapper.DepartmentToModelMapper;

public class DepartmentRepositoryImpl implements DepartmentRepository {

    private final DepartmentReadRepository repository;

    private final DepartmentToModelMapper toModelMapper = getMapper(DepartmentToModelMapper.class);

    public DepartmentRepositoryImpl(final DepartmentReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByDepartmentCode(final DepartmentCode departmentCode) {
        final Optional<DepartmentEntity> department = this.repository.findByDepartmentCode(departmentCode.getValue());
        return department.isPresent();
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return this.repository
                .findByDepartmentCode(departmentCode.getValue())
                .map(toModelMapper::map)
                .orElse(null);
    }
}
