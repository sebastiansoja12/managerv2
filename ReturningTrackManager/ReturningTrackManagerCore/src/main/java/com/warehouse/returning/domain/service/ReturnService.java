package com.warehouse.returning.domain.service;

import com.warehouse.returning.domain.enumeration.ReasonCode;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ShipmentId;

public interface ReturnService {

    ReturnPackage getReturn(final ReturnPackageId returnId);

    boolean existsForShipment(final ShipmentId shipmentId);

    void deleteReturn(final ReturnPackageId returnPackageId);

    void changeReasonCode(final ReturnPackageId returnPackageId, final ReasonCode reasonCode);

    ReturnPackageId nextReturnPackageId();

    void saveOrUpdate(final ReturnPackage returnPackage);
}
