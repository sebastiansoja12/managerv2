package com.warehouse.voronoi;

import com.warehouse.voronoi.domain.model.Coordinates;
import com.warehouse.voronoi.domain.model.Department;

import java.util.List;

public class DepotInMemoryData {

    public static List<Department> buildDepots() {

        // WROCŁAW
        final Department department = Department.builder()
                .departmentCode("WRO")
                .coordinates(Coordinates.builder()
                        .lon(51.1271647)
                        .lat(16.9218245)
                        .build())
                .build();

        // WARSZAWA
        final Department department2 = Department.builder()
                .departmentCode("WA1")
                .coordinates(Coordinates.builder()
                        .lon(52.2328001)
                        .lat(20.7741284)
                        .build())
                .build();

        // LUBLIN
        final Department department3 = Department.builder()
                .departmentCode("LUB")
                .coordinates(Coordinates.builder()
                        .lon(51.2181956)
                        .lat(22.4937308)
                        .build())
                .build();

        // GLIWICE
        final Department department4 = Department.builder()
                .departmentCode("KT1")
                .coordinates(Coordinates.builder()
                        .lon(50.3013283)
                        .lat(18.5795769)
                        .build())
                .build();

        // POZNAŃ
        final Department department5 = Department.builder()
                .departmentCode("POZ")
                .coordinates(Coordinates.builder()
                        .lon(52.4080281)
                        .lat(16.7615841)
                        .build())
                .build();

        // KRAKÓW
        final Department department6 = Department.builder()
                .departmentCode("KR1")
                .coordinates(Coordinates.builder()
                        .lon(50.0468548)
                        .lat(19.9348336)
                        .build())
                .build();

        // GDAŃSK
        final Department department7 = Department.builder()
                .departmentCode("GD1")
                .coordinates(Coordinates.builder()
                        .lon(54.3612063)
                        .lat(18.5499457)
                        .build())
                .build();


        // ŁÓDŹ
        final Department department8 = Department.builder()
                .departmentCode("NCS")
                .coordinates(Coordinates.builder()
                        .lon(51.7732033)
                        .lat(19.4105532)
                        .build())
                .build();

        // CZĘSTOCHOWA
        final Department department9 = Department.builder()
                .departmentCode("CZW")
                .coordinates(Coordinates.builder()
                        .lon(50.8094082)
                        .lat(19.0527317)
                        .build())
                .build();

        // BYDGOSZCZ
        final Department department10 = Department.builder()
                .departmentCode("BYD")
                .coordinates(Coordinates.builder()
                        .lon(53.1257696)
                        .lat(17.9681431)
                        .build())
                .build();

        // ZIELONA GÓRA
        final Department department11 = Department.builder()
                .departmentCode("ZGR")
                .coordinates(Coordinates.builder()
                        .lon(51.927740)
                        .lat(15.3813419)
                        .build())
                .build();

        // KIELCE
        final Department department12 = Department.builder()
                .departmentCode("KIE")
                .coordinates(Coordinates.builder()
                        .lon(50.8541274)
                        .lat(20.5456019)
                        .build())
                .build();
        // RZESZÓW
        final Department department13 = Department.builder()
                .departmentCode("RZE")
                .coordinates(Coordinates.builder()
                        .lon(50.0136105)
                        .lat(21.8363533)
                        .build())
                .build();

        // OLSZTYN
        final Department department14 = Department.builder()
                .departmentCode("OLS")
                .coordinates(Coordinates.builder()
                        .lon(53.7760917)
                        .lat(20.3956595)
                        .build())
                .build();

        // SZCZECIN
        final Department department15 = Department.builder()
                .departmentCode("SZC")
                .coordinates(Coordinates.builder()
                        .lon(53.4298189)
                        .lat(14.484542)
                        .build())
                .build();

        // CIECHANÓW
        final Department department16 = Department.builder()
                .departmentCode("CIE")
                .coordinates(Coordinates.builder()
                        .lon(52.8710294)
                        .lat(20.5821742)
                        .build())
                .build();

        // GRUDZIĄDZ
        final Department department17 = Department.builder()
                .departmentCode("GRU")
                .coordinates(Coordinates.builder()
                        .lon(53.4730137)
                        .lat(18.71262)
                        .build())
                .build();


        return List.of(department, department2, department3, department4, department5, department6, department7, department8, department9, department10, department11,
                department12, department13, department14, department15, department16, department17);
    }
}
