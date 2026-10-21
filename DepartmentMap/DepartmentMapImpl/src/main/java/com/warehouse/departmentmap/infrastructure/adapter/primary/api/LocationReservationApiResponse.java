package com.warehouse.departmentmap.infrastructure.adapter.primary.api;

import java.time.Instant;

public record LocationReservationApiResponse(String palletTrackingNumber,
                                             String note,
                                             Long reservedByUserId,
                                             Instant reservedAt) {
}
