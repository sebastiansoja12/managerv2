package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.application.port.secondary.ReturnReadModelRepository;
import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.vo.ReturnPackageSnapshot;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnReadModelEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnReadModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ReturnReadModelRepositoryImpl implements ReturnReadModelRepository {

    private final ReturnReadModelEntityRepository repository;

    public ReturnReadModelRepositoryImpl(final ReturnReadModelEntityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sync(final ReturnPackageSnapshot snapshot) {
        repository.save(ReturnReadModelMapper.map(snapshot));
    }

    @Override
    public ReturnPage findByDepartmentIdAndOperatorId(final DepartmentId departmentId, final OperatorId operatorId,
                                                      final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        final Page<ReturnReadModelEntity> result = repository.findByDepartmentIdAndOperatorId(
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId(
                        departmentId.value()),
                new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.OperatorId(
                        operatorId.value()),
                pageRequest);
        return new ReturnPage(
                result.getContent().stream().map(ReturnReadModelMapper::map).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }
}
