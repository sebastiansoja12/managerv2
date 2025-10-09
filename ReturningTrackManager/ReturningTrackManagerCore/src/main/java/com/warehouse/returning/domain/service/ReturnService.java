package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.model.ReturnPackageRequest;
import com.warehouse.returning.domain.vo.ReturnId;
import com.warehouse.returning.domain.vo.ReturnModel;
import com.warehouse.returning.domain.vo.ReturnPackageId;

public interface ReturnService {

    ReturnPackageRequest unlockReturn(ReturnPackageRequest request);

    ReturnModel getReturn(ReturnId returnId);

    void deleteReturn(final ReturnPackageId returnPackageId);

    ReturnPackageId nextReturnPackageId();

    void saveOrUpdate(final ReturnPackage returnPackage);
}
