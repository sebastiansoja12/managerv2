package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.domain.enumeration.DayAvailability;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record OpeningScheduleExceptionApi(
        @NotNull LocalDate date,
        @NotNull DayAvailability availability,
        @NotNull List<@Valid OpeningIntervalApi> intervals) {
}
