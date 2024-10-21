package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ZebraInitializeRequestDto {
    Long parcelId;
    ProcessTypeDto processType;
}
