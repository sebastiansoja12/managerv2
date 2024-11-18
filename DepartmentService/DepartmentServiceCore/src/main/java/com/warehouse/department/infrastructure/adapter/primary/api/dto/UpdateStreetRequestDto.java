package com.warehouse.department.infrastructure.adapter.primary.api.dto;

public class UpdateStreetRequestDto {

    private String depotCode;

    private String street;

    public UpdateStreetRequestDto() {

    }

    public UpdateStreetRequestDto(String depotCode, String street) {
        this.depotCode = depotCode;
        this.street = street;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public String getStreet() {
        return street;
    }
}
