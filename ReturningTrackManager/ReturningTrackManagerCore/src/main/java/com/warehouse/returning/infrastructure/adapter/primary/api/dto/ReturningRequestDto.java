package com.warehouse.returning.infrastructure.adapter.primary.api.dto;


import java.util.List;

import lombok.Data;

@Data
public class ReturningRequestDto {
    private List<ReturnPackageRequestDto> requests;
    private DepotCodeDto depotCode;
    private UsernameDto username;
}
