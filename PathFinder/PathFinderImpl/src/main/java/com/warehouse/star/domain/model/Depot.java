package com.warehouse.star.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.util.Objects;

@Data
@Value
@Builder
public class Depot {
    String city;

    String street;

    String country;

    String depotCode;

    Coordinates coordinates;

    public boolean getDepotCode(String depotCode) {
        return Objects.equals(this.depotCode, depotCode);
    }
}