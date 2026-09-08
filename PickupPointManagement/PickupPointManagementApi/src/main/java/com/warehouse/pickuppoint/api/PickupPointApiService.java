package com.warehouse.pickuppoint.api;

import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionRequestDto;

import java.util.List;
import java.util.Optional;

public interface PickupPointApiService {

    Optional<PickupPointSelectionDto> getById(final PickupPointIdDto pickupPointId);

    List<PickupPointSelectionDto> getByIds(final List<PickupPointIdDto> pickupPointIds);

    PickupPointSelectionDto validateSelection(final PickupPointSelectionRequestDto selectionRequest);
}
