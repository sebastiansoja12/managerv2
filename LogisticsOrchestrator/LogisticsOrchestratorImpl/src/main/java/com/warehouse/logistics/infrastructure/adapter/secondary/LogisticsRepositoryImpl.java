package com.warehouse.logistics.infrastructure.adapter.secondary;

import org.mapstruct.factory.Mappers;

import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.LogisticsResponse;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DeliveryEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.entity.DepartmentEntity;
import com.warehouse.logistics.infrastructure.adapter.secondary.mapper.DeliveryEntityMapper;

public class LogisticsRepositoryImpl implements LogisticsRepository {

    private final LogisticsReadRepository repository;

    private final DepartmentReadRepository departmentReadRepository;

    private final DeliveryEntityMapper mapper = Mappers.getMapper(DeliveryEntityMapper.class);

    public LogisticsRepositoryImpl(final LogisticsReadRepository repository,
                                   final DepartmentReadRepository departmentReadRepository) {
        this.repository = repository;
        this.departmentReadRepository = departmentReadRepository;
    }

    @Override
    public LogisticsResponse create(final LogisticsRequest logistics) {
        final DepartmentEntity department = departmentReadRepository
                .findByDepartmentCode(logistics.getDepartmentCode().getValue())
                .orElseThrow();
        final DeliveryEntity entity = mapper.map(logistics, department.getDepartmentId());
        repository.save(entity);
        return mapper.map(entity);
    }
}
