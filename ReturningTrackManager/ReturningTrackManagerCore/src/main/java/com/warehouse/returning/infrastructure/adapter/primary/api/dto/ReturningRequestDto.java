package com.warehouse.returning.infrastructure.adapter.primary.api.dto;


import java.util.List;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ReturningRequestDto {
    List<ReturnPackageRequestDto> requests;
    DepotCodeDto depotCode;
    UsernameDto username;
}
