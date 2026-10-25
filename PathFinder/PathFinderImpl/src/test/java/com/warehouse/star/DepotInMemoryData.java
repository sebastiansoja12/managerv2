package com.warehouse.star;

import com.warehouse.star.domain.model.Coordinates;
import com.warehouse.star.domain.model.Depot;

import java.util.List;

public class DepotInMemoryData {

    public static List<Depot> buildDepots() {

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

        // GDAŃSK
        final Depot depot7 = Depot.builder()
                .depotCode("GD1")
                .coordinates(Coordinates.builder()
                        .lon(54.3612063)
                        .lat(18.5499457)
                        .build())
                .build();


        // ŁÓDŹ
        final Depot depot8 = Depot.builder()
                .depotCode("NCS")
                .coordinates(Coordinates.builder()
                        .lon(51.7732033)
                        .lat(19.4105532)
                        .build())
                .build();

        // CZĘSTOCHOWA
        final Depot depot9 = Depot.builder()
                .depotCode("CZW")
                .coordinates(Coordinates.builder()
                        .lon(50.8094082)
                        .lat(19.0527317)
                        .build())
                .build();

        // BYDGOSZCZ
        final Depot depot10 = Depot.builder()
                .depotCode("BYD")
                .coordinates(Coordinates.builder()
                        .lon(53.1257696)
                        .lat(17.9681431)
                        .build())
                .build();

        // ZIELONA GÓRA
        final Depot depot11 = Depot.builder()
                .depotCode("ZGR")
                .coordinates(Coordinates.builder()
                        .lon(51.927740)
                        .lat(15.3813419)
                        .build())
                .build();

        // KIELCE
        final Depot depot12 = Depot.builder()
                .depotCode("KIE")
                .coordinates(Coordinates.builder()
                        .lon(50.8541274)
                        .lat(20.5456019)
                        .build())
                .build();
        // RZESZÓW
        final Depot depot13 = Depot.builder()
                .depotCode("RZE")
                .coordinates(Coordinates.builder()
                        .lon(50.0136105)
                        .lat(21.8363533)
                        .build())
                .build();

        // OLSZTYN
        final Depot depot14 = Depot.builder()
                .depotCode("OLS")
                .coordinates(Coordinates.builder()
                        .lon(53.7760917)
                        .lat(20.3956595)
                        .build())
                .build();

        // SZCZECIN
        final Depot depot15 = Depot.builder()
                .depotCode("SZC")
                .coordinates(Coordinates.builder()
                        .lon(53.4298189)
                        .lat(14.484542)
                        .build())
                .build();

        // CIECHANÓW
        final Depot depot16 = Depot.builder()
                .depotCode("CIE")
                .coordinates(Coordinates.builder()
                        .lon(52.8710294)
                        .lat(20.5821742)
                        .build())
                .build();

        // GRUDZIĄDZ
        final Depot depot17 = Depot.builder()
                .depotCode("GRU")
                .coordinates(Coordinates.builder()
                        .lon(53.4730137)
                        .lat(18.71262)
                        .build())
                .build();


        return List.of(depot, depot2, depot3, depot4, depot5, depot6, depot7, depot8, depot9, depot10, depot11,
                depot12, depot13, depot14, depot15, depot16, depot17);
    }
}
