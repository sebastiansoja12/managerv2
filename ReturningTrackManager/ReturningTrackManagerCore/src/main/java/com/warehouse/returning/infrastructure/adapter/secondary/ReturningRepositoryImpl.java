package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.model.ReturnStatus;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ProcessReturn;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToEntityMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToModelMapper;

public class ReturningRepositoryImpl implements ReturnRepository {

    private final ReturnReadRepository repository;

    public ReturningRepositoryImpl(final ReturnReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProcessReturn save(ReturnPackageRequest returnPackage) {
        return null;
    }

    @Override
    public ProcessReturn update(ReturnPackageRequest returnPackage) {
        return null;
    }

    @Override
    public ReturnPackage findById(final ReturnPackageId returnPackageId) {
		return this.repository
				.findById(ReturnId.of(returnPackageId))
				.map(ReturnPackageToModelMapper::map)
                .orElse(null);
    }

    @Override
    public ReturnStatus unlockReturn(Long parcelId, String returnToken) {
        return null;
    }

    @Override
    public void createOrUpdate(final ReturnPackage returnPackage) {
        final ReturnPackageEntity returnPackageEntity = ReturnPackageToEntityMapper.map(returnPackage);
        this.repository.save(returnPackageEntity);
    }

    @Override
    public void delete(final ReturnId returnId) {

    }
}
