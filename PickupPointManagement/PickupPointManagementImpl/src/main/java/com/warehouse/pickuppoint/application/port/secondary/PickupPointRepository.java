package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.vo.PickupPointCode;

import java.util.List;
import java.util.Optional;

public interface PickupPointRepository {

    Optional<PickupPoint> findById(final PickupPointId pickupPointId);

    Optional<PickupPoint> findByIdForSelection(final PickupPointId pickupPointId);

    List<PickupPoint> findByIds(final List<PickupPointId> pickupPointIds);

    boolean existsByCode(final PickupPointCode code);

    PickupPoint save(final PickupPoint pickupPoint);
}
