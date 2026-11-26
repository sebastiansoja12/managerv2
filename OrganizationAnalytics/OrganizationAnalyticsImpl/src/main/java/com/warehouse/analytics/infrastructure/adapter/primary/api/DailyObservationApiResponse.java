package com.warehouse.analytics.infrastructure.adapter.primary.api;

import java.time.LocalDate;

public record DailyObservationApiResponse(LocalDate day, long registeredShipments) {
}
