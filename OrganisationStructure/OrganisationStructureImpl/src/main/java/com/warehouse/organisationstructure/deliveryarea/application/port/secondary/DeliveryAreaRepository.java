package com.warehouse.organisationstructure.deliveryarea.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.deliveryarea.domain.model.DeliveryArea;
import com.warehouse.commonassets.identificator.DeliveryAreaId;

import java.util.List;
import java.util.Optional;

public interface DeliveryAreaRepository {

    List<DeliveryArea> findAll();

    List<DeliveryArea> findByDepartmentId(final DepartmentId departmentId);

    Optional<DeliveryArea> findById(final DeliveryAreaId deliveryAreaId);

    DeliveryArea create(final DeliveryArea deliveryArea);

    DeliveryArea update(final DeliveryArea deliveryArea);

    void delete(final DeliveryAreaId deliveryAreaId);
}
