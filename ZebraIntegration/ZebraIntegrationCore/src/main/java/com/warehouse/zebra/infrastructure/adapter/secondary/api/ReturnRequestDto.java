package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReturnRequestDto {
    List<ReturnPackageRequestDto> requests;
    DepotCodeDto depotCode;
    UsernameDto username;
}
