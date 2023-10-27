package com.warehouse.returning.infrastructure.adapter.secondary;

import static org.mapstruct.factory.Mappers.getMapper;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturningRepositoryImpl implements ReturnRepository {

    private final ReturnReadRepository repository;

    private final ReturnMapper returnMapper = getMapper(ReturnMapper.class);

    @Override
    public ProcessReturn save(ReturnPackage returnPackage) {
        final ReturnEntity returnEntity = returnMapper.map(returnPackage);
        repository.save(returnEntity);
        return returnMapper.map(returnEntity);
    }
}
