package com.warehouse.voronoi.domain.model;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Depot {
    String city;

    String street;

    String country;

    String depotCode;

    Coordinates coordinates;

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }
}