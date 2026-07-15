package com.warehouse.pickuppoint.application.port.secondary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.pickuppoint.application.port.primary.command.SearchPickupPointsCommand;

import java.util.Set;

public interface PickupPointSearchRepository {

    PickupPointSearchPage search(
            final SearchPickupPointsCommand command,
            final Set<DepartmentId> eligibleDepartmentIds);
}
