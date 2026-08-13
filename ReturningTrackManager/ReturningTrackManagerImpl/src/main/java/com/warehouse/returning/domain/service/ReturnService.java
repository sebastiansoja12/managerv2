package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.vo.DepartmentId;
import com.warehouse.returning.domain.vo.OperatorId;
import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.domain.vo.ShipmentId;

import java.util.Optional;

public interface ReturnService {

    ReturnPackage getReturn(final ReturnPackageId returnId);

    Optional<ReturnPackage> findLatestReturn(final ShipmentId shipmentId, final OperatorId operatorId);

    ReturnPage getReturns(final DepartmentId departmentId, final OperatorId operatorId, final int page, final int size);

    boolean existsForShipment(final ShipmentId shipmentId);


    void changeReasonCode(final ReturnPackageId returnPackageId, final ReasonCode reasonCode);

    ReturnPackageId nextReturnPackageId();

    void saveOrUpdate(final ReturnPackage returnPackage);

    void completeReturn(final ReturnPackageId returnPackageId);


    ReturnPackage findByShipmentId(final ShipmentId shipmentId);
}
