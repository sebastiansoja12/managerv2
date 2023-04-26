package com.warehouse.tsp.service;

import com.warehouse.tsp.domain.model.Coordinates;
import com.warehouse.tsp.domain.model.Depot;

import java.util.List;

public class DepotInMemoryData {

    public static List<Depot> depots() {

        // WROCŁAW
        final Depot depot = Depot.builder()
                .depotCode("WRO")
                .coordinates(Coordinates.builder()
                        .lon(51.1271647)
                        .lat(16.9218245)
                        .build())
                .build();

        // WARSZAWA
        final Depot depot2 = Depot.builder()
                .depotCode("WA1")
                .coordinates(Coordinates.builder()
                        .lon(52.2328001)
                        .lat(20.7741284)
                        .build())
                .build();

        // LUBLIN
        final Depot depot3 = Depot.builder()
                .depotCode("LUB")
                .coordinates(Coordinates.builder()
                        .lon(51.2181956)
                        .lat(22.4937308)
                        .build())
                .build();

        // GLIWICE
        final Depot depot4 = Depot.builder()
                .depotCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        // POZNAŃ
        final Depot depot5 = Depot.builder()
                .depotCode("POZ")
                .coordinates(Coordinates.builder()
                        .lon(52.4080281)
                        .lat(16.7615841)
                        .build())
                .build();

        // KRAKÓW
        final Depot depot6 = Depot.builder()
                .depotCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        return List.of(depot, depot2, depot3, depot4, depot5, depot6);
    }
}
