package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.exception.ReturnPackageNotFoundException;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToEntityMapper;
import com.warehouse.returning.infrastructure.adapter.secondary.mapper.ReturnPackageToModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Optional;

public class ReturningRepositoryImpl implements ReturnRepository {

    private final ReturnReadRepository repository;

    public ReturningRepositoryImpl(final ReturnReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReturnPackage findForProcessing(final ReturnPackageId returnPackageId) {
        return this.repository.findForProcessing(ReturnId.of(returnPackageId))
                .map(ReturnPackageToModelMapper::map)
                .orElseThrow(ReturnPackageNotFoundException::new);
    }

    @Override
    public ReturnPackage findDetailsById(final ReturnPackageId returnPackageId) {
        return this.repository.findDetailsById(ReturnId.of(returnPackageId))
                .map(ReturnPackageToModelMapper::map)
                .orElseThrow(ReturnPackageNotFoundException::new);
    }

    @Override
    public ReturnPackage findById(final ReturnPackageId returnPackageId) {
		return this.repository
				.findById(ReturnId.of(returnPackageId))
				.map(ReturnPackageToModelMapper::map)
                .orElseThrow(ReturnPackageNotFoundException::new);
    }

    @Override
    public ReturnPackage findByShipmentId(final ShipmentId shipmentId) {
        final Optional<ReturnPackageEntity> returnPackage = this.repository.findByShipmentId(shipmentId);
        return returnPackage.map(ReturnPackageToModelMapper::map).orElse(null);
    }

    @Override
    public Optional<ReturnPackage> findLatestByShipmentIdAndOperatorId(
            final ShipmentId shipmentId, final OperatorId operatorId) {
        return this.repository.findLatestByShipmentIdAndOperatorId(shipmentId, operatorId, PageRequest.of(0, 1))
                .stream().findFirst().map(ReturnPackageToModelMapper::map);
    }

    @Override
    public ReturnPage findByDepartmentIdAndOperatorId(
            final DepartmentId departmentId, final OperatorId operatorId, final int page, final int size) {
        final PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "updatedAt"));
        final Page<ReturnPackageEntity> result = this.repository.findByDepartmentIdAndOperatorId(
                departmentId, operatorId, pageRequest);
        return new ReturnPage(
                result.getContent().stream().map(ReturnPackageToModelMapper::map).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages());
    }

    @Override
    public void createOrUpdate(final ReturnPackage returnPackage) {
        final ReturnPackageEntity returnPackageEntity = ReturnPackageToEntityMapper.map(returnPackage);
        this.repository.save(returnPackageEntity);
    }

    @Override
    public boolean existsForShipment(final ShipmentId shipmentId) {
        final Optional<ReturnPackageEntity> returnPackage = this.repository.findByShipmentId(shipmentId);
        return returnPackage.isPresent();
    }
}
