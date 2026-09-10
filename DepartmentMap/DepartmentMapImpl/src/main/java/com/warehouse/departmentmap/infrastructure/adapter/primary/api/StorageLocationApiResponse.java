package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.util.List;
import java.util.UUID;

public record StorageLocationApiResponse(UUID locationId,
                                         UUID mapId,
                                         String code,
                                         int capacity,
                                         String status,
                                         List<PalletPlacementApiResponse> pallets,
                                         LocationReservationApiResponse reservation,
                                         Long version) {
}
