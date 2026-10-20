package com.warehouse.logisticsdelivery.application.port.secondary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryType;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryTarget;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryRepository {

    Optional<Delivery> findById(final DeliveryId deliveryId);

    Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type);

    Optional<Delivery> findActiveByTargetAndType(final DeliveryTarget target, final DeliveryType type);

    List<Delivery> findRecent(final int offset, final int limit);

    List<Delivery> findByDepartment(final DepartmentId departmentId, final int offset, final int limit);

    List<Delivery> findAllCreatedBetween(final LocalDateTime createdFromInclusive,
                                         final LocalDateTime createdToExclusive);

    List<Delivery> findByTarget(final DeliveryTarget target);

    List<Delivery> findReplacedBy(final DeliveryId replacingDeliveryId);

    void createOrUpdate(final Delivery delivery);
}
