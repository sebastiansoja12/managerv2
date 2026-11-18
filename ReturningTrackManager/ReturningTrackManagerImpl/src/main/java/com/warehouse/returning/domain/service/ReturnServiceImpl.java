package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.domain.vo.ShipmentId;

import java.util.Optional;
import java.util.UUID;

public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    public ReturnServiceImpl(final ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

    @Override
    public Optional<ReturnPackage> findLatestReturn(final ShipmentId shipmentId, final OperatorId operatorId) {
        return this.returnRepository.findLatestByShipmentIdAndOperatorId(
                shipmentId, operatorId);
    }

    @Override
    public ReturnPackage getReturn(final ReturnPackageId returnId) {
        return this.returnRepository.findDetailsById(returnId);
    }

    @Override
    public ReturnPage getReturns(
            final DepartmentId departmentId, final OperatorId operatorId, final int page, final int size) {
        return this.returnRepository.findByDepartmentIdAndOperatorId(departmentId, operatorId, page, size);
    }

    @Override
    public boolean existsForShipment(final ShipmentId shipmentId) {
		return returnRepository.existsForShipment(shipmentId);
    }

    @Override
    public ReturnPackageId nextReturnPackageId() {
        return new ReturnPackageId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    @Override
    public void saveOrUpdate(final ReturnPackage returnPackage) {
        this.returnRepository.createOrUpdate(returnPackage);
    }

    @Override
    public ReturnPackage findByShipmentId(final ShipmentId shipmentId) {
		return this.returnRepository.findByShipmentId(shipmentId);
    }
}
