package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.event.ReturnPackageCompleted;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.registry.DomainRegistry;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.domain.vo.ShipmentId;

import java.time.Instant;
import java.util.UUID;

public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    public ReturnServiceImpl(final ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

    @Override
    public ReturnPackage getReturn(final ReturnPackageId returnId) {
        return this.returnRepository.findById(returnId);
    }

    @Override
    public ReturnPage getReturns(
            final DepartmentCode departmentCode, final Long operatorId, final int page, final int size) {
        return this.returnRepository.findByDepartmentCodeAndOperatorId(departmentCode, operatorId, page, size);
    }

    @Override
    public boolean existsForShipment(final ShipmentId shipmentId) {
		return returnRepository.existsForShipment(
				new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(
						shipmentId.value()));
    }

    @Override
    public void deleteReturn(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = this.returnRepository.findById(returnPackageId);
        returnPackage.markAsCanceled();
        this.saveOrUpdate(returnPackage);
        DomainRegistry.publish(new ReturnPackageCanceled(returnPackage.toSnapshot(), Instant.now()));
    }

    @Override
    public void changeReasonCode(final ReturnPackageId returnPackageId, final ReasonCode reasonCode) {
        final ReturnPackage returnPackage = this.returnRepository.findById(returnPackageId);
        returnPackage.changeReasonCode(reasonCode);
        this.saveOrUpdate(returnPackage);
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
    public void completeReturn(final ShipmentId shipmentId) {
        final ReturnPackage returnPackage = this.findByShipmentId(shipmentId);
        returnPackage.markAsCompleted();
        this.saveOrUpdate(returnPackage);
        DomainRegistry.publish(new ReturnPackageCompleted(returnPackage.toSnapshot(), Instant.now()));
    }

    @Override
    public void cancelReturn(final ShipmentId shipmentId) {
        final ReturnPackage returnPackage = this.findByShipmentId(shipmentId);
        returnPackage.markAsCanceled();
        this.saveOrUpdate(returnPackage);
        DomainRegistry.publish(new ReturnPackageCanceled(returnPackage.toSnapshot(), Instant.now()));
    }

    @Override
    public ReturnPackage findByShipmentId(final ShipmentId shipmentId) {
		return this.returnRepository.findByShipmentId(
				new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(
						shipmentId.value()));
    }
}
