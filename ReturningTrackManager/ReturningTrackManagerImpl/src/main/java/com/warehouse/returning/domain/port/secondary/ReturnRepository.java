package com.warehouse.returning.domain.port.secondary;

import com.warehouse.common.DepartmentId;
import com.warehouse.common.OperatorId;
import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.domain.vo.ReturnPage;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;

import java.util.Optional;

public interface ReturnRepository {

    ReturnPackage findForProcessing(final ReturnPackageId returnPackageId);

    ReturnPackage findById(final ReturnPackageId returnPackageId);

    ReturnPackage findDetailsById(final ReturnPackageId returnPackageId);

    ReturnPackage findByShipmentId(final ShipmentId shipmentId);

    Optional<ReturnPackage> findLatestByShipmentIdAndOperatorId(final ShipmentId shipmentId, final OperatorId operatorId);

    ReturnPage findByDepartmentIdAndOperatorId(
            final DepartmentId departmentId, final OperatorId operatorId, final int page, final int size);

    void createOrUpdate(final ReturnPackage returnPackage);

    boolean existsForShipment(final ShipmentId shipmentId);
}
