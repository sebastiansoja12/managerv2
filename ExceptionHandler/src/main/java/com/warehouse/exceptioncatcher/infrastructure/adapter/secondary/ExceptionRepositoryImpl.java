package com.warehouse.exceptioncatcher.infrastructure.adapter.secondary;

import com.warehouse.exceptioncatcher.domain.port.secondary.ExceptionRepository;
import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.entity.ExceptionEntity;
import com.warehouse.exceptioncatcher.infrastructure.adapter.secondary.mapper.ExceptionEntityMapper;
import com.warehouse.exceptionhandler.exception.RestException;

import lombok.AllArgsConstructor;

import static org.mapstruct.factory.Mappers.getMapper;

@AllArgsConstructor
public class ExceptionRepositoryImpl implements ExceptionRepository {

    private final ExceptionReadRepository exceptionReadRepository;

    private final ExceptionEntityMapper exceptionEntityMapper = getMapper(ExceptionEntityMapper.class);

    @Override
    public void saveException(RestException ex) {
        final ExceptionEntity exceptionEntity = exceptionEntityMapper.map(ex);
        exceptionReadRepository.save(exceptionEntity);
    }
}
