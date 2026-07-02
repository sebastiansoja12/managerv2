package com.warehouse.pickuppoint.infrastructure.adapter.primary.api;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

public record OpeningIntervalApi(
        @Min(0) @Max(1439) int startMinute,
        @Min(1) @Max(1440) int endMinute) {
}
