package com.warehouse.shipment.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.shipment.domain.vo.Address;

import java.util.Optional;

public interface DeliveryAreaServicePort {

    Optional<DepartmentId> findDeliveryDepartment(final Address address);
}
