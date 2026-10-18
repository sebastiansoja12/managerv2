package com.warehouse.driverhub.api;

import com.warehouse.driverhub.api.dto.CarrierDto;

import java.util.List;
import java.util.Optional;

public interface CarrierApiService {
    List<CarrierDto> findCarriers(final boolean activeOnly);

    Optional<CarrierDto> findActiveCarrier(final Long carrierId);
}
