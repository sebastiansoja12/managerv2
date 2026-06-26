package com.warehouse.pickuppoint.application.port.primary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.application.port.primary.command.CreatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;
import com.warehouse.pickuppoint.application.port.primary.command.UpdatePickupPointCommand;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointPageResult;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;

import java.util.List;
import java.util.Optional;

public interface PickupPointPort {

    PickupPointResult create(final CreatePickupPointCommand command);

    PickupPointResult update(final UpdatePickupPointCommand command);

    PickupPointResult changeStatus(final ChangePickupPointStatusCommand command);

    Optional<PickupPointResult> get(final PickupPointId pickupPointId);

    List<PickupPointResult> get(final List<PickupPointId> pickupPointIds);

    PickupPointPageResult search(final SearchPickupPointsCommand command);

    PickupPointResult validateSelection(
            final PickupPointId pickupPointId,
            final PickupPointSelectionCriteria criteria);
}
