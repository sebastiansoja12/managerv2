package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.DepartmentCode;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ShipmentId;

public interface ReturnService {

    ReturnPackage getReturn(final ReturnPackageId returnId);

    ReturnPage getReturns(final DepartmentCode departmentCode, final Long operatorId, final int page, final int size);

    boolean existsForShipment(final ShipmentId shipmentId);

    void deleteReturn(final ReturnPackageId returnPackageId);

    void changeReasonCode(final ReturnPackageId returnPackageId, final ReasonCode reasonCode);

    ReturnPackageId nextReturnPackageId();

    void saveOrUpdate(final ReturnPackage returnPackage);

    void completeReturn(final ShipmentId shipmentId);

    ReturnPackage findByShipmentId(final ShipmentId shipmentId);
}
