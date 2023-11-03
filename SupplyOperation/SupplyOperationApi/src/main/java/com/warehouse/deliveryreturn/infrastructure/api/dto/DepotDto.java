package com.warehouse.deliveryreturn.infrastructure.api.dto;

import lombok.Data;

@Data
public class DepotDto {

    private Long id;

    private String city;

    private String street;

    private String country;

    private String depotCode;

}
