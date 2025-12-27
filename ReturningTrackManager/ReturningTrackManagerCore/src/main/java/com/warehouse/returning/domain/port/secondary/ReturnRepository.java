package com.warehouse.returning.domain.port.secondary;

import com.warehouse.returning.domain.model.ReturnPackage;
import com.warehouse.returning.domain.vo.ReturnPackageId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;

public interface ReturnRepository {

    ReturnPackage findById(final ReturnPackageId returnPackageId);

    ReturnPackage findByShipmentId(final ShipmentId shipmentId);

    void createOrUpdate(final ReturnPackage returnPackage);

    boolean existsForShipment(final ShipmentId shipmentId);
}
