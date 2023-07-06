package com.warehouse.voronoi.domain.model;


import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Data
@Value
@Builder
public class Depot {
    String city;

    String street;

    String country;

    String depotCode;

    Coordinates coordinates;
}