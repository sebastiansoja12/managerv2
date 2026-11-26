package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.domain.vo.AssignedCourier;
import com.warehouse.logisticsdelivery.domain.vo.Courier;

import java.util.List;
import java.util.Optional;

public interface CourierServicePort {

    List<Courier> findCouriers(final DepartmentId departmentId);

    Optional<AssignedCourier> findCourier(final SupplierId supplierId);
}
