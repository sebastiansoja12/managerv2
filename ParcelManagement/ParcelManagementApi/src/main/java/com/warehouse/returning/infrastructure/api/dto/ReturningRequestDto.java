package com.warehouse.returning.infrastructure.api.dto;


import lombok.Data;

import java.util.List;

@Data
public class ReturningRequestDto {
    private List<ReturnPackageRequestDto> requests;
    private DepotCodeDto depotCode;
    private UsernameDto username;
}
