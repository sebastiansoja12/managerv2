package com.warehouse.returning.domain.service;

import java.util.UUID;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.port.secondary.ReturnRepository;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnPackageId;

public class ReturnServiceImpl implements ReturnService {

    private final ReturnRepository returnRepository;

    public ReturnServiceImpl(final ReturnRepository returnRepository) {
        this.returnRepository = returnRepository;
    }

	@Override
	public ReturnPackageRequest unlockReturn(ReturnPackageRequest request) {
		return null;
	}

    @Override
    public ReturnModel getReturn(ReturnId returnId) {
        return null;
    }

    @Override
    public void deleteReturn(final ReturnPackageId returnPackageId) {
        final ReturnPackage returnPackage = this.returnRepository.findById(returnPackageId);
        returnPackage.markAsDeleted();
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
}
