package com.warehouse.zebra.infrastructure.adapter.secondary.api;

import lombok.Data;

import java.util.List;

@Data
public class ReturnRequestDto {
    private List<ReturnPackageRequestDto> requests;
    private DepotCodeDto depotCode;
    private UsernameDto username;
}
