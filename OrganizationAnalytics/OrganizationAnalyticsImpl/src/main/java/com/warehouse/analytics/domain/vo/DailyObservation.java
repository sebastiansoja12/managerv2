package com.warehouse.analytics.domain.vo;

import java.time.LocalDate;

public record DailyObservation(LocalDate day, long registeredShipments) {
}
