package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import com.warehouse.pickuppoint.domain.enumeration.OpeningScheduleMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OpeningScheduleApi(
        @NotBlank String timeZone,
        @NotNull OpeningScheduleMode mode,
        @NotNull List<@Valid OpeningDayApi> days,
        @NotNull List<@Valid OpeningScheduleExceptionApi> exceptions) {
}
