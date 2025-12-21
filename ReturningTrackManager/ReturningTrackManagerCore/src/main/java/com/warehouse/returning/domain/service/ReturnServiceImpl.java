package com.warehouse.returning.domain.service;

import java.time.Instant;
import java.util.UUID;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.event.ReturnPackageCanceled;
import com.warehouse.returning.domain.event.ReturnPackageCompleted;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.registry.DomainRegistry;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ShipmentId;

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
    public boolean existsForShipment(final ShipmentId shipmentId) {
		return returnRepository.existsForShipment(
				new com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId(
						shipmentId.value()));
    }

    @Override
    public void deleteReturn(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = this.returnRepository.findById(returnPackageId);
        returnPackage.markAsDeleted();
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
    public void completeReturn(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = this.returnRepository.findById(returnPackageId);
        returnPackage.markAsCompleted();
        this.saveOrUpdate(returnPackage);
        DomainRegistry.publish(new ReturnPackageCompleted(returnPackage.toSnapshot(), Instant.now()));
    }
}
