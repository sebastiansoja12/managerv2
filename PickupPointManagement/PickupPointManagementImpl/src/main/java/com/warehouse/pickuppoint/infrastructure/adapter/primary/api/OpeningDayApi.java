package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public record OpeningDayApi(
        @NotNull DayOfWeek dayOfWeek,
        @NotNull DayAvailability availability,
        @NotNull List<@Valid OpeningIntervalApi> intervals) {
}
