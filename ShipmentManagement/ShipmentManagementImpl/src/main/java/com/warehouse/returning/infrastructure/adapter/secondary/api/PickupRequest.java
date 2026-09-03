package com.warehouse.returning.infrastructure.adapter.secondary.api;

import java.util.UUID;
import com.warehouse.commonassets.identificator.DepartmentId;

public record PickupRequest(UUID pickupId, DepartmentId scanDepartmentId) {
}
